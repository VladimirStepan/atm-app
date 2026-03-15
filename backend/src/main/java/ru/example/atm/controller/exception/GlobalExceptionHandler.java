package ru.example.atm.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.atm.exception.AlreadyExistsException;
import ru.example.atm.exception.NotFoundException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExists(AlreadyExistsException ex) {
        return ResponseEntity.status(409).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409).body(Map.of("error", ex.getMessage()));
    }
}
