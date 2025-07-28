package ru.yandex.practicum.commerce.delivery.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.commerce.delivery.entity.Delivery;

@Component
public class DeliveryMapper {

    public static DeliveryDto mapDeliveryToDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId().toString())
                .fromAddress(AddressMapper.mapAddressToDto(delivery.getFromAddress()))
                .toAddress(AddressMapper.mapAddressToDto(delivery.getToAddress()))
                .orderId(delivery.getOrderId().toString())
                .deliveryState(delivery.getDeliveryState().name())
                .build();
    }
}
