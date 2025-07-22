package ru.yandex.practicum.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.DeliveryState;
import ru.yandex.practicum.validation.EnumValid;
import ru.yandex.practicum.validation.ValidUUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDto {

    private String deliveryId;

    @Valid
    private AddressDto fromAddress;

    @Valid
    private AddressDto toAddress;

    @ValidUUID
    private String orderId;

    @EnumValid(enumClass = DeliveryState.class)
    private String deliveryState;
}
