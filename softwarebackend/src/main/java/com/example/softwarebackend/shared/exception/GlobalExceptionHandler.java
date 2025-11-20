package com.example.softwarebackend.shared.exception;

import com.example.softwarebackend.shared.dto.response.ApiResponseDTO;
import jakarta.xml.bind.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice()
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add( error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>("400", errors.stream().findFirst().orElse("Validation Error"), errors.toString(), false));

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("ResourceNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(404).body(new ApiResponseDTO<>("404", ex.getMessage(), null, false));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        log.warn("ValidationException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>("400", ex.getMessage(), null, false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new ApiResponseDTO<>("400", ex.getMessage(), null, false));
    }
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<?> handleAccessException(IllegalAccessException ex) {
        log.error("IllegalAccessException: {}", ex.getMessage());
        return ResponseEntity.status(403).body(new ApiResponseDTO<>("403", "IO operation failed: " + ex.getMessage(), null, false));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException ex) {
        log.error("IOException: {}", ex.getMessage());
        return ResponseEntity.status(500).body(new ApiResponseDTO<>("500", "IO operation failed: " + ex.getMessage(), null, false));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access Denied: {}", ex.getMessage());
        return ResponseEntity.status(403).body(new ApiResponseDTO<>("403", "Access Denied: You don't have permission to access this resource", null, false));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage());
        return ResponseEntity.status(500).body(new ApiResponseDTO<>("500", "Internal Server Error", null, false));
    }


}
