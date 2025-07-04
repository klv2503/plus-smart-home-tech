package ru.yandex.practicum.commerce.shoppingstore.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.shoppingstore.enums.SortParam;
import ru.yandex.practicum.commerce.shoppingstore.validation.EnumValid;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pageable {

    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 1;

    @EnumValid(enumClass = SortParam.class)
    private String sort;

}
