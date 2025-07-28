package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class ProductNotFoundException extends SmartHomeException {

    public ProductNotFoundException(String obj, UUID productId) {
        super(
                String.format("'%s' with ID '%s' not found in the database.", obj, productId),
                String.format("Requested '%s' was not found.", obj),
                String.valueOf(HttpStatus.NOT_FOUND)
        );
    }
}