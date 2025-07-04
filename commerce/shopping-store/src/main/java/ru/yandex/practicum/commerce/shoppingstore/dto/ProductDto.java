package ru.yandex.practicum.commerce.shoppingstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.enums.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.enums.QuantityState;
import ru.yandex.practicum.commerce.shoppingstore.validation.EnumValid;
import ru.yandex.practicum.validation.OnCreate;
import ru.yandex.practicum.validation.OnUpdate;
import ru.yandex.practicum.validation.ValidUUID;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    @ValidUUID(groups = OnUpdate.class)
    private String productId; //Идентификатор товара в БД

    @NotBlank(groups = OnCreate.class)
    private String productName; //Наименование товара *

    @NotBlank(groups = OnCreate.class)
    private String description; //Описание товара *

    private String imageSrc; //Ссылка на картинку во внешнем хранилище или SVG

    @NotNull(groups = OnCreate.class)
    @EnumValid(enumClass = QuantityState.class)
    private String quantityState; //Статус, перечисляющий состояние остатка как свойства товара *

    @NotNull(groups = OnCreate.class)
    @EnumValid(enumClass = ProductState.class)
    private String productState; //Статус товара *

    @NotNull(groups = OnCreate.class)
    @EnumValid(enumClass = ProductCategory.class)
    private String productCategory; //Категория товара

    @DecimalMin("1.0")
    @NotNull(groups = OnCreate.class)
    private BigDecimal price; //Цена товара *

}
