package com.portfolio.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler for the entire application.
 *
 * KEY CONCEPTS:
 * - @RestControllerAdvice: Intercepts exceptions thrown by ANY controller
 *   and converts them into proper HTTP responses.
 * - Without this, unhandled exceptions would return ugly 500 errors with
 *   stack traces (a security risk and poor user experience).
 * - Each @ExceptionHandler method handles a specific exception type.
 *
 * This is a BEST PRACTICE in professional Spring Boot applications.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles TaskNotFoundException → returns HTTP 404 (Not Found).
     *
     * Triggered when a client requests a task ID that doesn't exist.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation errors → returns HTTP 400 (Bad Request).
     *
     * Triggered when @Valid fails on a request body (e.g., blank title).
     * Collects all validation error messages into a single response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Collect all field validation error messages
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed — " + errorMessages,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles IllegalArgumentException → returns HTTP 400 (Bad Request).
     *
     * Triggered when invalid enum values or arguments are provided.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catch-all handler for any unexpected exceptions → returns HTTP 500.
     *
     * This ensures the client always receives a clean JSON response,
     * even for unforeseen errors. In production, you would also log the
     * exception details for debugging.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
