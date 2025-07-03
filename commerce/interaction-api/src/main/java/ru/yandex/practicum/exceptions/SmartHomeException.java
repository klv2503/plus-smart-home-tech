package ru.yandex.practicum.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmartHomeException extends RuntimeException {
    private String httpStatus;
    private String userMessage;

    public SmartHomeException(String message, String userMessage, String httpStatus) {
        super(message);
        this.userMessage = userMessage;
        this.httpStatus = httpStatus;
    }

}
