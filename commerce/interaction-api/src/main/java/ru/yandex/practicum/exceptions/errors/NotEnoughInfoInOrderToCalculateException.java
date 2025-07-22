package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class NotEnoughInfoInOrderToCalculateException extends SmartHomeException {

    public NotEnoughInfoInOrderToCalculateException(UUID orderId) {
        super(
                String.format("There is not enough info in Order '%s' to calculate total price.", orderId),
                "Information in order is not enough.",
                String.valueOf(HttpStatus.BAD_REQUEST)
        );
    }
}
