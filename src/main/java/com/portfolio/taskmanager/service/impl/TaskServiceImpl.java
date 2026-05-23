package com.portfolio.taskmanager.service.impl;

import com.portfolio.taskmanager.dto.TaskRequest;
import com.portfolio.taskmanager.dto.TaskResponse;
import com.portfolio.taskmanager.exception.TaskNotFoundException;
import com.portfolio.taskmanager.model.Priority;
import com.portfolio.taskmanager.model.Task;
import com.portfolio.taskmanager.model.TaskStatus;
import com.portfolio.taskmanager.repository.TaskRepository;
import com.portfolio.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the TaskService interface.
 *
 * KEY CONCEPTS:
 * - @Service: Marks this class as a Spring service bean (a specialized @Component).
 *   Spring will auto-detect it and make it available for dependency injection.
 * - @RequiredArgsConstructor (Lombok): Generates a constructor for all 'final' fields.
 *   This is the recommended way to do dependency injection in Spring (constructor injection).
 * - Business logic lives HERE, not in the controller. The controller only handles
 *   HTTP concerns (request/response); the service handles the "what to do" logic.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    // Spring injects the TaskRepository automatically via constructor injection
    private final TaskRepository taskRepository;

    /**
     * Creates a new task from the request data and saves it to the database.
     *
     * FLOW:
     * 1. Build a Task entity from the DTO fields.
     * 2. If no priority is specified, default to MEDIUM.
     * 3. Save to database (JPA auto-generates ID and timestamps).
     * 4. Convert the saved entity back to a response DTO.
     */
    @Override
    public TaskResponse createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .status(TaskStatus.PENDING) // New tasks always start as PENDING
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(savedTask);
    }

    /**
     * Retrieves all tasks, ordered by creation date (newest first).
     *
     * The stream().map().collect() pattern converts each Task entity
     * to a TaskResponse DTO before returning the list.
     */
    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(TaskResponse::fromEntity) // Method reference — shorthand for task -> TaskResponse.fromEntity(task)
                .collect(Collectors.toList());
    }

    /**
     * Finds a single task by ID or throws a 404 exception.
     *
     * orElseThrow() is a clean way to handle "not found" cases with Optional.
     */
    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponse.fromEntity(task);
    }

    /**
     * Updates an existing task's fields.
     *
     * IMPORTANT: We first fetch the existing task, then update only the fields
     * provided in the request. This preserves the original status, timestamps,
     * and any other fields not included in the update request.
     */
    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // Update fields from the request
        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());

        if (request.getPriority() != null) {
            existingTask.setPriority(request.getPriority());
        }

        Task updatedTask = taskRepository.save(existingTask);
        return TaskResponse.fromEntity(updatedTask);
    }

    /**
     * Deletes a task by ID.
     *
     * We first check if the task exists to provide a meaningful 404 error.
     * Without this check, deleteById() would silently succeed even if the
     * task doesn't exist.
     */
    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    /**
     * Marks a task as COMPLETED.
     *
     * This is a dedicated endpoint because changing status is a common
     * and important operation that deserves its own clear API action
     * (rather than requiring a full update request).
     */
    @Override
    public TaskResponse markTaskAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setStatus(TaskStatus.COMPLETED);

        Task completedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(completedTask);
    }

    /**
     * Filters tasks by status.
     *
     * We convert the status string to the TaskStatus enum, which also
     * validates that the provided status is valid. If not, an
     * IllegalArgumentException is thrown and caught by the global handler.
     */
    @Override
    public List<TaskResponse> getTasksByStatus(String status) {
        TaskStatus taskStatus;
        try {
            taskStatus = TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid status: '" + status + "'. Valid values are: PENDING, IN_PROGRESS, COMPLETED"
            );
        }

        return taskRepository.findByStatus(taskStatus)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
