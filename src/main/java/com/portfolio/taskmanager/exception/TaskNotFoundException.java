package com.portfolio.taskmanager.exception;

/**
 * Custom exception thrown when a task cannot be found by its ID.
 *
 * WHY A CUSTOM EXCEPTION?
 * - It makes the code more readable: "throw new TaskNotFoundException(id)"
 *   clearly communicates what happened.
 * - It allows the GlobalExceptionHandler to catch this specific exception
 *   and return a proper 404 response.
 * - It extends RuntimeException (unchecked), so we don't need to declare
 *   it in method signatures with "throws".
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }
}
