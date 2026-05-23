package com.portfolio.taskmanager.dto;

import com.portfolio.taskmanager.model.Priority;
import com.portfolio.taskmanager.model.Task;
import com.portfolio.taskmanager.model.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for outgoing task responses sent to the client.
 *
 * WHY A SEPARATE RESPONSE DTO?
 * - We might want to format or rename fields differently for the API consumer.
 * - We can exclude sensitive or internal fields from the response.
 * - The static factory method fromEntity() provides a clean conversion pattern.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Factory method to convert a Task entity into a TaskResponse DTO.
     *
     * This pattern keeps the conversion logic in one place, making the code
     * easier to maintain. If a new field is added to Task, we only need
     * to update this method.
     *
     * @param task the Task entity from the database
     * @return a TaskResponse DTO ready to be sent to the client
     */
    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
