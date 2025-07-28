package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.validation.ValidUUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippedToDeliveryRequest {

    @ValidUUID
    private String orderId;

    @ValidUUID
    private String deliveryId;
}
