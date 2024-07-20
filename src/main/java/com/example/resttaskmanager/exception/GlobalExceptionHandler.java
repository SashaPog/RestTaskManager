package com.example.resttaskmanager.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("errors", ex.getBindingResult().getAllErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.warn(exception.getMessage(), exception);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>>
    handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.warn(exception.getMessage(), exception);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NullEntityReferenceException.class)
    public ResponseEntity<?> handleNullEntityReferenceException(NullEntityReferenceException ex) {
        log.warn(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "BAD_REQUEST");
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "NOT_FOUND");
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn(ex.getMessage(), ex);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "FORBIDDEN");
        response.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleInternalServerError(Exception exception) {
        log.warn(exception.getMessage(), exception);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "INTERNAL_SERVER_ERROR");
        response.put("error", exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

