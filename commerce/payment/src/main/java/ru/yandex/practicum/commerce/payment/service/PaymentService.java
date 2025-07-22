package ru.yandex.practicum.commerce.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.commerce.payment.client.PaymentOrderFeign;
import ru.yandex.practicum.commerce.payment.client.PaymentStoreFeign;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.commerce.payment.entity.Payment;
import ru.yandex.practicum.commerce.payment.entity.PaymentState;
import ru.yandex.practicum.commerce.payment.repository.PaymentsRepository;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShortProduct;
import ru.yandex.practicum.exceptions.errors.NoOrderFoundException;
import ru.yandex.practicum.exceptions.errors.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.exceptions.errors.OperationNotAllowedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentsRepository repository;
    private final PaymentStoreFeign storeFeign;
    private final PaymentOrderFeign orderFeign;

    public PaymentDto payment(OrderDto order) {
        //Здесь и далее хотел добавить проверку, что список продуктов не null и не empty,
        //но IDEA утверждает, что я аннотациями это варианты запретил и проверки не приняла
        if (order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(UUID.fromString(order.getOrderId()));
        }
        order.setProductPrice(productCost(order));
        order.setTotalPrice(getTotalCost(order));
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .orderId(UUID.fromString(order.getOrderId()))
                .state(PaymentState.PENDING)
                .build();
        repository.save(payment);
        BigDecimal fee = order.getProductPrice().multiply(BigDecimal.valueOf(0.1));
        return PaymentDto.builder()
                .paymentId(payment.getId().toString())
                .totalPayment(order.getProductPrice().add(order.getDeliveryPrice()).add(fee))
                .productCost(order.getProductPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .feeTotal(fee)
                .build();
    }

    public BigDecimal getTotalCost(OrderDto order) {
        //Пока к методу обращение только из payment, где проверка выполняется,
        //но здесь продублировано на случай обращения из других мест кода
        if (order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(UUID.fromString(order.getOrderId()));
        }
        return order.getProductPrice()
                .add(order.getProductPrice().multiply(BigDecimal.valueOf(0.1)))
                .add(order.getDeliveryPrice());
    }

    public void orderPaid(String orderId) {
        UUID id = UUID.fromString(orderId);
        Payment payment = repository.findByOrderId(id)
                .orElseThrow(() -> new NoOrderFoundException(id));
        if (PaymentState.SUCCESS.equals(payment.getState())) {
            throw new OperationNotAllowedException(id, "Order was earlier paid",
                    "Order was earlier successfully paid, new pay is blocked");
        }
        payment.setState(PaymentState.SUCCESS);
        orderFeign.paymentSuccess(orderId);
        repository.save(payment);
    }

    public BigDecimal productCost(OrderDto order) {
        List<UUID> ids = order.getProducts().keySet().stream()
                .map(UUID::fromString)
                .toList();
        List<ShortProduct> prods = storeFeign.getProduct(ids).getBody();
        if (CollectionUtils.isEmpty(prods) || (prods.size() < ids.size())) {
            throw new NotEnoughInfoInOrderToCalculateException(UUID.fromString(order.getOrderId()));
        }
        var result = BigDecimal.ZERO;
        for (ShortProduct prod : prods) {
            result = result.add(prod.getPrice()
                    .multiply(BigDecimal.valueOf(order.getProducts().get(prod.getProductId().toString()))));
        }
        return result;
    }

    public void failedPayment(String orderId) {
        UUID id = UUID.fromString(orderId);
        Payment payment = repository.findByOrderId(id)
                .orElseThrow(() -> new NoOrderFoundException(id));
        if (PaymentState.SUCCESS.equals(payment.getState())) {
            throw new OperationNotAllowedException(id, "Order was earlier paid",
                    "Order was earlier successfully paid, fail is blocked.");
        }
        payment.setState(PaymentState.FAILED);
        orderFeign.paymentFailed(orderId);
        repository.save(payment);
    }

}
