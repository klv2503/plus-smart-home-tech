package ru.yandex.practicum.exceptions.errors;

import lombok.Getter;
import ru.yandex.practicum.exceptions.SmartHomeException;

import java.util.Map;

@Getter
public class ValidationFailedException extends SmartHomeException {
    private final Map<String, String> fieldErrors;

    public ValidationFailedException(Map<String, String> fieldErrors) {
        super("Validation failed", "Некорректные данные", "400");
        this.fieldErrors = fieldErrors;
    }

}