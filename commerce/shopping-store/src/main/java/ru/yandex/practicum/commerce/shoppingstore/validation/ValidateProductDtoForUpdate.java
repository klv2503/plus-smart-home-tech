package ru.yandex.practicum.commerce.shoppingstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;
import ru.yandex.practicum.dto.ProductDto;

import java.math.BigDecimal;

public class ValidateProductDtoForUpdate implements ConstraintValidator<NotEmptyProductDto, ProductDto> {
    @Override
    public boolean isValid(ProductDto dto, ConstraintValidatorContext constraintValidatorContext) {
        return Strings.isNotBlank(dto.getProductName())
                || Strings.isNotBlank(dto.getDescription())
                || Strings.isNotBlank(dto.getImageSrc())
                || dto.getQuantityState() != null
                || dto.getProductState() != null
                || dto.getProductCategory() != null
                || (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ONE) >= 0);
    }
}
