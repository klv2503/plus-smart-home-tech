package ru.yandex.practicum.exceptions.errors;

import org.springframework.http.HttpStatus;
import ru.yandex.practicum.exceptions.SmartHomeException;

public class ServiceUnavailableException extends SmartHomeException {

    public ServiceUnavailableException(String serviceName) {
        super(String.format("'%s' is currently unavailable.", serviceName),
                String.format("Requested service '%s' is currently unavailable.", serviceName),
                String.valueOf(HttpStatus.SERVICE_UNAVAILABLE));
    }
}
