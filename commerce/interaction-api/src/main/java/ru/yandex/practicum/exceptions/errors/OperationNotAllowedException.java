package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.UUID;

public class OperationNotAllowedException extends SmartHomeException {
    public OperationNotAllowedException(UUID id, String grund, String message) {
        super(grund, message, String.valueOf(HttpStatus.FORBIDDEN));
    }
}
