package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.commerce.order.client.OrderDeliveryFeign;
import ru.yandex.practicum.commerce.order.client.OrderPaymentFeign;
import ru.yandex.practicum.commerce.order.client.OrderWarehouseFeign;
import ru.yandex.practicum.commerce.order.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.commerce.order.entity.Order;
import ru.yandex.practicum.commerce.order.mapper.OrderMapper;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.exceptions.errors.NoOrderFoundException;
import ru.yandex.practicum.exceptions.errors.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.utiliteis.KeySetMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderCartService shoppingCartService;
    private final OrderWarehouseFeign warehouseFeign;
    private final OrderDeliveryFeign deliveryFeign;
    private final OrderPaymentFeign paymentFeign;

    private final OrderRepository repository;

    @Transactional(readOnly = true)
    public Order getOrder(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NoOrderFoundException(id));
    }

    public OrderDto createNewOrder(CreateNewOrderRequest params) {
        UUID cartId = UUID.fromString(params.getShopCartDto().getShoppingCartId());
        //Для начала проверяем наличие cart : нет корзины - нет и проблемы
        CartSpecialDto specialDto = shoppingCartService.getCartById(cartId);
        Order order = new Order();

        order.setOrderId(UUID.randomUUID());
        order.setShoppingCartId(cartId);
        //Строго говоря, продукты следовало бы взять из модуля shopping-cart,
        //специально спросил наставника на семинаре, делаю как он сказал
        order.setProducts(params.getShopCartDto().getProducts());
        order.setState(OrderState.NEW);

        order.setUsername(specialDto.getUserName());
        log.info("\nOrder with userName {}", order);
        AssemblyProductsForOrderRequest request = AssemblyProductsForOrderRequest.builder()
                .products(KeySetMapper.mapIndexesUUIDtoString(params.getShopCartDto().getProducts()))
                .orderId(order.getOrderId().toString())
                .build();
        BookedProductsDto deliveryParams = warehouseFeign.assemblyProductForOrderFromShoppingCart(request).getBody();
        order.setDeliveryWeight(deliveryParams.getDeliveryWeight());
        order.setDeliveryVolume(deliveryParams.getDeliveryVolume());
        order.setFragile(deliveryParams.getFragile());
        log.info("\nOrder with deliveryParams {}", order);

        AddressDto fromAddress = warehouseFeign.getWarehouseAddress().getBody();

        DeliveryDto deliveryDto = deliveryFeign.planDelivery(
                DeliveryDto.builder()
                        .fromAddress(fromAddress)
                        .toAddress(params.getAddress())
                        .orderId(order.getOrderId().toString())
                        .build()
        ).getBody();
        order.setDeliveryId(UUID.fromString(deliveryDto.getDeliveryId()));
        log.info("\nOrder with deliveryId {}", order);

        OrderDto orderDto = OrderMapper.orderToDto(order);
        BigDecimal deliveryPrice = deliveryFeign.deliveryCost(orderDto).getBody();
        order.setDeliveryPrice(deliveryPrice);
        orderDto.setDeliveryPrice(deliveryPrice);
        log.info("\nOrder with deliveryPrice {}", order);

        PaymentDto payment = paymentFeign.payment(orderDto).getBody();
        order.setProductPrice(payment.getProductCost());
        order.setTotalPrice(payment.getTotalPayment());
        order.setPaymentId(UUID.fromString(payment.getPaymentId()));
        log.info("\nFull order {}", order);

        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getUsersOrders(String userName) {
        List<Order> orders = repository.findAllByUsername(userName);
        Set<UUID> ids = orders.stream()
                .map(Order::getOrderId)
                .collect(Collectors.toSet());
        List<OrderBookingDto> booked = warehouseFeign.getBooked(ids).getBody();
        Map<UUID, List<OrderBookingDto>> byOrder = booked.stream()
                .collect(Collectors.groupingBy(OrderBookingDto::getOrderId));
        orders.forEach(order -> {
            byOrder.getOrDefault(order.getOrderId(), List.of())
                    .forEach(b -> order.getProducts().put(b.getProductId(), b.getQuantity()));
        });
        return OrderMapper.ordersListToDtoList(orders);
    }

    public OrderDto returnOrder(ProductReturnRequest request) {
        Map<UUID, Integer> newValues = warehouseFeign.returnProducts(request).getBody();
        log.info("\nreturnOrder: newValues {}", newValues);
        boolean areProductsReturned = newValues.values().stream()
                .allMatch(qty -> qty == 0);
        Order order = getOrder(UUID.fromString(request.getOrderId()));
        order.setState(areProductsReturned ? OrderState.PRODUCT_RETURNED : OrderState.ON_DELIVERY);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto paymentSuccess(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.PAID);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto paymentFailed(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.PAYMENT_FAILED);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto delivery(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.DELIVERED);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto deliveryFailed(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.DELIVERY_FAILED);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto completeOrder(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.COMPLETED);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto calculateTotal(String orderId) {
        UUID id = UUID.fromString(orderId);
        Order order = getOrder(UUID.fromString(orderId));
        //Здесь и в следующем методе берем продукты из зарезервированных на складе,
        //чтобы не хранить их в двух местах БД
        List<OrderBookingDto> prods = warehouseFeign.getBooked(Set.of(id)).getBody();
        if (prods == null || prods.isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(id);
        }
        prods.forEach(ordBook ->
                order.getProducts().put(ordBook.getProductId(), ordBook.getQuantity())
        );
        order.setProductPrice(paymentFeign.productCost(OrderMapper.orderToDto(order)).getBody());
        order.setTotalPrice(paymentFeign.getTotalCost(OrderMapper.orderToDto(order)).getBody());
        order.setState(OrderState.ON_PAYMENT);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto calculateDelivery(String orderId) {
        UUID id = UUID.fromString(orderId);
        Order order = getOrder(id);
        List<OrderBookingDto> prods = warehouseFeign.getBooked(Set.of(id)).getBody();
        if (CollectionUtils.isEmpty(prods)) {
            throw new NotEnoughInfoInOrderToCalculateException(id);
        }
        prods.forEach(ordBook ->
                order.getProducts().put(ordBook.getProductId(), ordBook.getQuantity())
        );
        AssemblyProductsForOrderRequest request = AssemblyProductsForOrderRequest.builder()
                .products(KeySetMapper.mapIndexesUUIDtoString(order.getProducts()))
                .orderId(order.getOrderId().toString())
                .build();
        BookedProductsDto deliveryParams = warehouseFeign.assemblyProductForOrderFromShoppingCart(request).getBody();
        order.setDeliveryWeight(deliveryParams.getDeliveryWeight());
        order.setDeliveryVolume(deliveryParams.getDeliveryVolume());
        order.setFragile(deliveryParams.getFragile());
        order.setDeliveryPrice(deliveryFeign.deliveryCost(OrderMapper.orderToDto(order)).getBody());
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto assemblyOrder(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.ASSEMBLED);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

    public OrderDto assemblyFailed(String orderId) {
        Order order = getOrder(UUID.fromString(orderId));
        order.setState(OrderState.ASSEMBLY_FAILED);
        repository.save(order);
        return OrderMapper.orderToDto(order);
    }

}
