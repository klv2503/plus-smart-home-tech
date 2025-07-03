package ru.yandex.practicum.commerce.shoppingstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

//Аннотация для проверки полей запроса на update Item - каждое из полей может быть пустым, но не все три одновременно
//За проверку отвечает ValidateItemDtoForUpdate
@Documented
@Constraint(validatedBy = ValidateProductDtoForUpdate.class)
@Target(value = {ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyProductDto {

    String message() default "At least one of (name, description and available) must not be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
