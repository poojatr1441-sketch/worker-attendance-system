package com.worker.tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = "VALIDATION_ERROR";

        String message = ex.getMessage();

        if (message != null) {

            if (message.contains("not found")) {
                status = HttpStatus.NOT_FOUND;
                errorCode = "RESOURCE_NOT_FOUND";
            }

            else if (
                    message.contains("already clocked in")
                    || message.contains("already settled")
            ) {
                status = HttpStatus.CONFLICT;
                errorCode = "CONFLICT";
            }
        }

        Map<String, Object> body = Map.of(
                "error", errorCode,
                "message", message,
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex
    ) {

        Map<String, Object> body = Map.of(
                "error", "INTERNAL_SERVER_ERROR",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR
        ).body(body);
    }
}