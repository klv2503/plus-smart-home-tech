package ru.yandex.practicum.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exceptions.errors.ValidationFailedException;
import ru.yandex.practicum.exceptions.response.ErrorResponse;

import java.io.IOException;
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

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        String responseBody = ex.contentUTF8();
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) status = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            ErrorResponse errorResponse = extractErrorResponse(responseBody);
            return ResponseEntity.status(status).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse fallback = new ErrorResponse(
                    new SmartHomeException(
                            "Remote call failed: " + ex.getMessage(),
                            "Удалённый сервис вернул ошибку в неизвестном формате",
                            status.value() + " " + status.name()
                    )
            );
            return ResponseEntity.status(status).body(fallback);
        }
    }

    private HttpStatus parseHttpStatus(String statusString) {
        // Ожидается формат типа "400 BAD_REQUEST"
        String codePart = statusString.split(" ")[0];
        return HttpStatus.valueOf(Integer.parseInt(codePart));
    }

    private ErrorResponse extractErrorResponse(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);

        if (root.isArray() && root.size() > 0) {
            root = root.get(0);
        }

        return mapper.treeToValue(root, ErrorResponse.class);
    }
}