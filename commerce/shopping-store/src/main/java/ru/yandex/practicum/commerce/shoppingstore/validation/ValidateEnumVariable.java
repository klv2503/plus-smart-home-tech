package ru.yandex.practicum.commerce.shoppingstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ValidateEnumVariable implements ConstraintValidator<EnumValid, String> {

    private Class<? extends Enum<?>> classToValidation;

    @Override
    public void initialize(EnumValid appliedAnnotation) {
        this.classToValidation = appliedAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String checkedValue, ConstraintValidatorContext constraintValidatorContext) {
        if (checkedValue == null) return true;

        String normalized = checkedValue.toUpperCase();
        return Arrays.stream(classToValidation.getEnumConstants())
                .anyMatch(e -> e.name().equals(normalized));
    }
}
