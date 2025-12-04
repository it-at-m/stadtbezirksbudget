package de.muenchen.stadtbezirksbudget.backend.common;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Map<String, Object>> handlePropertyReferenceException(final PropertyReferenceException ex) {
        log.warn("Invalid property reference: {}", ex.getPropertyName());
        return ResponseEntity.badRequest().body(
                Map.of(
                        "error", "Invalid property reference",
                        "invalidProperty", ex.getPropertyName()));
    }
}
