package ru.yandex.practicum.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.validation.ValidUUID;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReturnRequest {

    @ValidUUID
    private String orderId;

    private Map<@ValidUUID String, @Min(1) Integer> products;
}
