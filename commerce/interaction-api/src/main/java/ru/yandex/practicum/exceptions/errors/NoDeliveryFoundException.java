package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class NoDeliveryFoundException extends SmartHomeException {

    public NoDeliveryFoundException(UUID deliveryI) {
        super(
                String.format("Delivery with ID '%s' not found in the database.", deliveryI),
                "Requested delivery was not found.",
                String.valueOf(HttpStatus.NOT_FOUND)
        );
    }
}
