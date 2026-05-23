package com.portfolio.taskmanager.dto;

import com.portfolio.taskmanager.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) for incoming task creation/update requests.
 *
 * WHY USE A DTO?
 * - Security: We control exactly which fields the client can send.
 *   Without a DTO, a client could send an "id" or "createdAt" field and
 *   potentially overwrite values they shouldn't control.
 * - Validation: We apply validation rules (@NotBlank, @Size) here,
 *   keeping the Entity class clean and focused on database mapping.
 * - Decoupling: If the database schema changes, the API contract stays stable.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

    /** Title is required and must be between 1 and 100 characters */
    @NotBlank(message = "Title is required and cannot be empty")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    /** Description is optional but limited to 500 characters if provided */
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    /** Priority is optional — defaults to MEDIUM in the service layer if null */
    private Priority priority;
}
