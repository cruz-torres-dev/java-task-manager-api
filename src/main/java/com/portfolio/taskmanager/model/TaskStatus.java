package com.portfolio.taskmanager.model;

/**
 * Enum representing the possible statuses of a task.
 *
 * Using an enum ensures type safety — only valid statuses can be assigned.
 * This prevents invalid values like typos ("complted") from reaching the database.
 */
public enum TaskStatus {

    /** Task has been created but work has not started yet */
    PENDING,

    /** Task is currently being worked on */
    IN_PROGRESS,

    /** Task has been finished successfully */
    COMPLETED
}
