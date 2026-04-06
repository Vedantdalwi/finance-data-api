package com.finance.dataprocessing.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finance.dataprocessing.common.exception.BaseException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBaseException(BaseException ex) {
    	ex.printStackTrace();
        return ResponseEntity.status(ex.getHttpStatus()).body(Map.of(
                "success", false,
                "error", ex.getErrorCode(),
                "message", ex.getMessage(),
                "timestamp", Instant.now()
        ));
    }

    // @PreAuthorize fail
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
    	ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "success", false,
                "error", "ACCESS_DENIED",
                "message", "You don't have access to perform this action",
                "timestamp", Instant.now()
        ));
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
    	ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "error", "INVALID_CREDENTIALS",
                "message", "Invalid email or password",
                "timestamp", Instant.now()
        ));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    	ex.printStackTrace();
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "success", false,
                "error", "VALIDATION_ERROR",
                "message", message,
                "timestamp", Instant.now()
        ));
    }

    // catch 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    	ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "INTERNAL_ERROR",
                "message", "Something went wrong",
                "timestamp", Instant.now()
        ));
    }
}
