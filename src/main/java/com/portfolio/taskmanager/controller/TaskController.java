package com.portfolio.taskmanager.controller;

import com.portfolio.taskmanager.dto.TaskRequest;
import com.portfolio.taskmanager.dto.TaskResponse;
import com.portfolio.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Task management endpoints.
 *
 * KEY CONCEPTS:
 * - @RestController: Combines @Controller + @ResponseBody. All methods return
 *   data directly (as JSON) instead of view templates.
 * - @RequestMapping("/api/v1/tasks"): Base URL for all endpoints in this controller.
 *   Using "/api/v1/" is a best practice for API versioning.
 * - @CrossOrigin: Allows requests from other origins (e.g., a frontend app).
 *   In production, you would restrict this to specific domains.
 *
 * HTTP METHOD MAPPING:
 * - POST   → Create a new resource
 * - GET    → Read/retrieve resources
 * - PUT    → Update an existing resource (full update)
 * - DELETE → Remove a resource
 * - PATCH  → Partial update (used here for marking as completed)
 */
@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TaskController {

    // Injected via constructor (thanks to @RequiredArgsConstructor)
    private final TaskService taskService;

    // ========================================================================
    // POST /api/v1/tasks — Create a new task
    // ========================================================================

    /**
     * Creates a new task.
     *
     * @Valid triggers the validation annotations on TaskRequest (@NotBlank, @Size).
     * If validation fails, Spring throws MethodArgumentNotValidException,
     * which is caught by our GlobalExceptionHandler.
     *
     * Returns HTTP 201 (Created) instead of 200 (OK) because a new resource
     * was created — this follows REST conventions.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        TaskResponse createdTask = taskService.createTask(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // ========================================================================
    // GET /api/v1/tasks — Retrieve all tasks
    // ========================================================================

    /**
     * Returns all tasks, ordered by creation date (newest first).
     *
     * Optional query parameter: ?status=PENDING to filter by status.
     * If no status is provided, all tasks are returned.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) String status) {

        List<TaskResponse> tasks;

        if (status != null && !status.isEmpty()) {
            tasks = taskService.getTasksByStatus(status);
        } else {
            tasks = taskService.getAllTasks();
        }

        return ResponseEntity.ok(tasks);
    }

    // ========================================================================
    // GET /api/v1/tasks/{id} — Retrieve a single task by ID
    // ========================================================================

    /**
     * Returns a single task by its ID.
     *
     * @PathVariable extracts the {id} from the URL path.
     * Returns HTTP 404 if the task doesn't exist (handled by exception handler).
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // ========================================================================
    // PUT /api/v1/tasks/{id} — Update an existing task
    // ========================================================================

    /**
     * Updates an existing task's title, description, and/or priority.
     *
     * PUT is used for full updates. The client should send all updatable fields.
     * Status is NOT updated here — use the PATCH endpoint for that.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        TaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    // ========================================================================
    // DELETE /api/v1/tasks/{id} — Delete a task
    // ========================================================================

    /**
     * Deletes a task by its ID.
     *
     * Returns HTTP 204 (No Content) because a successful delete has
     * no response body — the resource simply no longer exists.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    // ========================================================================
    // PATCH /api/v1/tasks/{id}/complete — Mark a task as completed
    // ========================================================================

    /**
     * Marks a task as completed.
     *
     * PATCH is used instead of PUT because we're making a partial update
     * (only changing the status field, not the entire resource).
     * This is a clean, RESTful design choice.
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> markTaskAsCompleted(@PathVariable Long id) {
        TaskResponse completedTask = taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok(completedTask);
    }
}
