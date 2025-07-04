package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class SpecifiedProductAlreadyInWarehouseException extends SmartHomeException {

    public SpecifiedProductAlreadyInWarehouseException(UUID productId) {
        super(
                String.format("Product with id '%s' is already registered in the warehouse.", productId),
                "This product already exists in the warehouse.",
                String.valueOf(HttpStatus.BAD_REQUEST)
        );
    }
}