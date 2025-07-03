package ru.yandex.practicum.dto;

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
public class AddProductToWarehouseRequest {

    @ValidUUID
    private String productId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
