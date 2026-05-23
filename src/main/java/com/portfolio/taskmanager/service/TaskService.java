package com.portfolio.taskmanager.service;

import com.portfolio.taskmanager.dto.TaskRequest;
import com.portfolio.taskmanager.dto.TaskResponse;

import java.util.List;

/**
 * Service interface defining the business operations for Task management.
 *
 * WHY USE AN INTERFACE?
 * - Decoupling: The controller depends on this interface, not the implementation.
 *   This means we can swap implementations (e.g., for testing) without changing
 *   the controller code.
 * - Testability: We can easily create mock implementations for unit tests.
 * - Clean Architecture: Follows the Dependency Inversion Principle (DIP) from SOLID.
 *   "Depend on abstractions, not on concretions."
 */
public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param request the task data from the client
     * @return the created task with its generated ID and timestamps
     */
    TaskResponse createTask(TaskRequest request);

    /**
     * Retrieves all tasks, ordered by creation date (newest first).
     *
     * @return list of all tasks
     */
    List<TaskResponse> getAllTasks();

    /**
     * Retrieves a single task by its ID.
     *
     * @param id the task ID
     * @return the task data
     * @throws com.portfolio.taskmanager.exception.TaskNotFoundException if the task doesn't exist
     */
    TaskResponse getTaskById(Long id);

    /**
     * Updates an existing task's title, description, and/or priority.
     *
     * @param id      the ID of the task to update
     * @param request the updated task data
     * @return the updated task
     * @throws com.portfolio.taskmanager.exception.TaskNotFoundException if the task doesn't exist
     */
    TaskResponse updateTask(Long id, TaskRequest request);

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     * @throws com.portfolio.taskmanager.exception.TaskNotFoundException if the task doesn't exist
     */
    void deleteTask(Long id);

    /**
     * Marks a task as completed by setting its status to COMPLETED.
     *
     * @param id the ID of the task to mark as completed
     * @return the updated task with COMPLETED status
     * @throws com.portfolio.taskmanager.exception.TaskNotFoundException if the task doesn't exist
     */
    TaskResponse markTaskAsCompleted(Long id);

    /**
     * Retrieves all tasks filtered by a specific status.
     *
     * @param status the status to filter by (PENDING, IN_PROGRESS, COMPLETED)
     * @return list of tasks matching the given status
     */
    List<TaskResponse> getTasksByStatus(String status);
}
