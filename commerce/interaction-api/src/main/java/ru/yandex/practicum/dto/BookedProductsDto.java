package ru.yandex.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookedProductsDto {

    private BigDecimal deliveryWeight;

    private BigDecimal deliveryVolume;

    private Boolean fragile;

}
