package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

public class NotAuthorizedUserException extends SmartHomeException {

    public NotAuthorizedUserException() {
        super(
                "User is not authorized.",
                "Authorization is required. Please log in.",
                String.valueOf(HttpStatus.UNAUTHORIZED)
        );
    }
}