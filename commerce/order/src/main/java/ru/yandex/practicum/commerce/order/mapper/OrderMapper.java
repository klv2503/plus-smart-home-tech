package ru.yandex.practicum.commerce.order.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.order.entity.Order;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.utiliteis.KeySetMapper;

import java.util.List;

@Component
public class OrderMapper {

    public static OrderDto orderToDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId() == null ? null : order.getOrderId().toString())
                .shoppingCartId(order.getShoppingCartId() == null ? null : order.getShoppingCartId().toString())
                .products(KeySetMapper.mapIndexesUUIDtoString(order.getProducts()))
                .paymentId(order.getPaymentId() == null ? null : order.getPaymentId().toString())
                .deliveryId(order.getDeliveryId() == null ? null : order.getDeliveryId().toString())
                .state(order.getState().name())
                .deliveryWeight(order.getDeliveryWeight() == null ? null : order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume() == null ? null : order.getDeliveryVolume())
                .fragile(order.getFragile() == null ? null : order.getFragile())
                .totalPrice(order.getTotalPrice() == null ? null : order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice() == null ? null : order.getDeliveryPrice())
                .productPrice(order.getProductPrice() == null ? null : order.getProductPrice())
                .build();
    }

    public static List<OrderDto> ordersListToDtoList(List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::orderToDto)
                .toList();
    }
}
