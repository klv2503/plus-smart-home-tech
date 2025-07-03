package ru.yandex.practicum.commerce.shoppingstore.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.shoppingstore.dto.ProductDto;
import ru.yandex.practicum.commerce.shoppingstore.entities.Product;
import ru.yandex.practicum.commerce.shoppingstore.enums.EnumUtils;
import ru.yandex.practicum.commerce.shoppingstore.enums.ProductCategory;
import ru.yandex.practicum.commerce.shoppingstore.enums.ProductState;
import ru.yandex.practicum.commerce.shoppingstore.enums.QuantityState;

import java.util.List;
import java.util.UUID;

@Component
public class ProductMapper {

    public static Product mapDtoToProduct(ProductDto dto) {
        return Product.builder()
                .productId(UUID.fromString(dto.getProductId()))
                .productName(dto.getProductName())
                .description(dto.getDescription())
                .imageSrc(dto.getImageSrc())
                .quantityState(EnumUtils.fromStringOrThrow(dto.getQuantityState(), QuantityState.class))
                .productState(EnumUtils.fromStringOrThrow(dto.getProductState(), ProductState.class))
                .productCategory(EnumUtils.fromStringOrThrow(dto.getProductCategory(), ProductCategory.class))
                .price(dto.getPrice())
                .build();
    }

    public static ProductDto mapProductToDto(Product product) {
        return ProductDto.builder()
                .productId(product.getProductId().toString())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState().name())
                .productState(product.getProductState().name())
                .productCategory(product.getProductCategory().name())
                .price(product.getPrice())
                .build();
    }

    public static List<ProductDto> mapProductListToDtoList(List<Product> products) {
        return products.stream()
                .map(ProductMapper::mapProductToDto)
                .toList();
    }
}
