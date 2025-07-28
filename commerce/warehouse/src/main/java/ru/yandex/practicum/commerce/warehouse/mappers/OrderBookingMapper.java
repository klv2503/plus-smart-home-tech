package ru.yandex.practicum.commerce.warehouse.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.warehouse.entity.OrderBooking;
import ru.yandex.practicum.dto.OrderBookingDto;

import java.util.List;

@Component
public class OrderBookingMapper {

    public static OrderBookingDto mapBookingToDto(OrderBooking booked) {
        return OrderBookingDto.builder()
                .orderId(booked.getOrderId())
                .productId(booked.getProductId())
                .deliveryId(booked.getDeliveryId())
                .quantity(booked.getQuantity())
                .build();
    }

    public static List<OrderBookingDto> mapListBookingToListDto(List<OrderBooking> bookedList) {
        return bookedList.stream()
                .map(OrderBookingMapper::mapBookingToDto)
                .toList();
    }
}
