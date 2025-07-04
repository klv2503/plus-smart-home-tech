package ru.yandex.practicum.exceptions.response;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class ThrowableDto {
    private final String message;
    private final String localizedMessage;
    private final List<StackTraceElementDto> stackTrace;

    public ThrowableDto(Throwable throwable) {
        this.message = throwable.getMessage();
        this.localizedMessage = throwable.getLocalizedMessage();
        this.stackTrace = Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElementDto::new)
                .toList();
    }
}
