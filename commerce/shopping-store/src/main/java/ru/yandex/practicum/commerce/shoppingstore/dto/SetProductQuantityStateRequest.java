package ru.yandex.practicum.commerce.shoppingstore.dto;

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
public class SetProductQuantityStateRequest {

    @ValidUUID
    private String productId;

    @NotNull
    private String quantityState;
}
