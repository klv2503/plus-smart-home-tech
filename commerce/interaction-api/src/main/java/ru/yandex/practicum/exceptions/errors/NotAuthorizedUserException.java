package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

public class NotAuthorizedUserException extends SmartHomeException {

    public NotAuthorizedUserException() {
        super(
                "Username is missing or empty.",
                "Authorization is required. Please log in.",
                String.valueOf(HttpStatus.UNAUTHORIZED)
        );
    }
}