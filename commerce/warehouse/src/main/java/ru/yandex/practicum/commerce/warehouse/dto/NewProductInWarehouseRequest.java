package ru.yandex.practicum.commerce.warehouse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.validation.ValidUUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProductInWarehouseRequest {

    @ValidUUID
    private String productId;

    private Boolean fragile;

    @Valid
    private DimensionDto dimension;

    @Min(1)
    @NotNull
    private Double weight;

}
