package ru.yandex.practicum.exceptions.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.exceptions.SmartHomeException;
import ru.yandex.practicum.exceptions.errors.ValidationFailedException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private final String userMessage;
    private final String httpStatus;
    private final List<StackTraceElementDto> stackTrace;
    private final ThrowableDto cause;
    private final List<ThrowableDto> suppressed;
    private final String localizedMessage;
    private final Map<String, String> validationErrors;

    public ErrorResponse(SmartHomeException e) {
        this.message = e.getMessage();
        this.userMessage = e.getUserMessage();
        this.httpStatus = e.getHttpStatus();
        this.localizedMessage = e.getLocalizedMessage();
        this.stackTrace = Arrays.stream(e.getStackTrace())
                .map(StackTraceElementDto::new)
                .toList();
        this.suppressed = Arrays.stream(e.getSuppressed())
                .map(ThrowableDto::new)
                .toList();
        this.cause = e.getCause() != null ? new ThrowableDto(e.getCause()) : null;
        if (e instanceof ValidationFailedException validationEx) {
            this.validationErrors = validationEx.getFieldErrors();
        } else {
            this.validationErrors = null;
        }

    }

}