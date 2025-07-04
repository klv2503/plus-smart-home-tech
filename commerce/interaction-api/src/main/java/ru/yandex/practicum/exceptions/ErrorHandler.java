package ru.yandex.practicum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exceptions.errors.ValidationFailedException;
import ru.yandex.practicum.exceptions.response.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(SmartHomeException.class)
    public ResponseEntity<ErrorResponse> handleSmartHomeException(SmartHomeException e) {
        HttpStatus status = parseHttpStatus(e.getHttpStatus());
        ErrorResponse errorResponse = new ErrorResponse(e);
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ValidationFailedException validationException = new ValidationFailedException(errors);
        return ResponseEntity.badRequest().body(new ErrorResponse(validationException));

    }

    private HttpStatus parseHttpStatus(String statusString) {
        // Ожидается формат типа "400 BAD_REQUEST"
        String codePart = statusString.split(" ")[0];
        return HttpStatus.valueOf(Integer.parseInt(codePart));
    }
}