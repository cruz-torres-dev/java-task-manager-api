package com.portfolio.taskmanager.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Standardized error response structure sent to the client when something goes wrong.
 *
 * Instead of returning raw exception messages (which may expose internal details),
 * we use this class to provide clean, consistent, and informative error responses.
 *
 * Example response:
 * {
 *     "status": 404,
 *     "message": "Task not found with id: 99",
 *     "timestamp": "2024-01-15T10:30:00"
 * }
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /** HTTP status code (e.g., 404, 400, 500) */
    private int status;

    /** Human-readable error message */
    private String message;

    /** When the error occurred */
    private LocalDateTime timestamp;
}
