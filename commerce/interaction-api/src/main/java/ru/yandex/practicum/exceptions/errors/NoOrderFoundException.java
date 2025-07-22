package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class NoOrderFoundException extends SmartHomeException {

    public NoOrderFoundException(UUID orderId) {
        super(
                String.format("Order with ID '%s' not found in the database.", orderId),
                "Requested order was not found.",
                String.valueOf(HttpStatus.NOT_FOUND)
        );
    }
}
