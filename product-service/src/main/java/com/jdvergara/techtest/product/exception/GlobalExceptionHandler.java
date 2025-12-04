package com.jdvergara.techtest.product.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request) {
        
        log.error("Product not found: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Producto no encontrado")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateProductException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateProduct(
            DuplicateProductException ex,
            HttpServletRequest request) {
        
        log.error("Duplicate product: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Producto duplicado")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        
        log.error("Data integrity violation: {}", ex.getMessage());
        
        String message = "El producto no se puede crear porque ya existe un producto con el mismo SKU";
        
        if (ex.getMessage() != null && ex.getMessage().contains("sku")) {
            message = "Ya existe un producto con el SKU proporcionado";
        }
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Violación de integridad de datos")
                .message(message)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Errores de validación");
        response.put("message", "Los datos proporcionados no son válidos");
        response.put("validationErrors", errors);
        response.put("path", request.getRequestURI());
        response.put("timestamp", LocalDateTime.now());
        
        log.error("Validation errors: {}", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        
        log.error("Unexpected error: ", ex);
        
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error interno del servidor")
                .message("Ocurrió un error inesperado. Por favor, contacte al administrador.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
