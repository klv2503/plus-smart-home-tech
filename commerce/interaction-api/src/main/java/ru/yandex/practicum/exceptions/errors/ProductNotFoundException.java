package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class ProductNotFoundException extends SmartHomeException {

    public ProductNotFoundException(UUID productId) {
        super(
                String.format("Product with ID '%s' not found in the database.", productId),
                "Requested product was not found.",
                String.valueOf(HttpStatus.NOT_FOUND)
        );
    }
}