package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.enums.OrderState;
import ru.yandex.practicum.validation.EnumValid;
import ru.yandex.practicum.validation.ValidUUID;

import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    @ValidUUID
    private String orderId;

    @ValidUUID
    private String shoppingCartId;

    @NotNull
    private Map<String, Integer> products;

    private String paymentId;

    private String deliveryId;

    @EnumValid(enumClass = OrderState.class)
    private String state;

    private BigDecimal deliveryWeight;

    private BigDecimal deliveryVolume;

    private Boolean fragile;

    private BigDecimal totalPrice;

    private BigDecimal deliveryPrice;

    private BigDecimal productPrice;
}
