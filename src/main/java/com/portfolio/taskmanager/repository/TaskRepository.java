package com.portfolio.taskmanager.repository;

import com.portfolio.taskmanager.model.Task;
import com.portfolio.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Task database operations.
 *
 * KEY CONCEPTS:
 * - By extending JpaRepository, Spring Data JPA automatically provides
 *   implementations for common operations: save(), findById(), findAll(),
 *   deleteById(), count(), existsById(), and many more.
 * - No implementation class is needed — Spring generates one at runtime!
 * - The generic parameters <Task, Long> mean: "this repository manages Task
 *   entities, and the primary key type is Long."
 *
 * CUSTOM QUERIES:
 * - Spring Data JPA can derive queries from method names.
 *   For example, findByStatus(TaskStatus status) generates:
 *   SELECT * FROM tasks WHERE status = ?
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find all tasks with a specific status.
     * Spring Data JPA auto-generates the query from the method name.
     *
     * @param status the status to filter by (e.g., PENDING, COMPLETED)
     * @return list of tasks matching the given status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find all tasks ordered by creation date (newest first).
     *
     * @return list of tasks sorted by createdAt descending
     */
    List<Task> findAllByOrderByCreatedAtDesc();
}
