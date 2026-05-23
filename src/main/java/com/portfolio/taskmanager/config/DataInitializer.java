package com.portfolio.taskmanager.config;

import com.portfolio.taskmanager.model.Priority;
import com.portfolio.taskmanager.model.Task;
import com.portfolio.taskmanager.model.TaskStatus;
import com.portfolio.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that loads sample tasks into the database on application startup.
 *
 * KEY CONCEPTS:
 * - CommandLineRunner: Spring Boot calls the run() method after the application starts.
 *   This is useful for loading initial data, running migrations, or setup tasks.
 * - These sample records make it easy to test the API immediately without
 *   having to create tasks manually via POST requests first.
 *
 * NOTE: Since we're using an in-memory H2 database, this data is re-created
 * every time the application restarts.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        // Only load sample data if the database is empty
        if (taskRepository.count() == 0) {

            Task task1 = Task.builder()
                    .title("Set up Spring Boot project")
                    .description("Initialize the project with Maven, add dependencies, and configure application properties.")
                    .status(TaskStatus.COMPLETED)
                    .priority(Priority.HIGH)
                    .build();

            Task task2 = Task.builder()
                    .title("Implement REST API endpoints")
                    .description("Create CRUD endpoints for task management with proper HTTP methods and status codes.")
                    .status(TaskStatus.IN_PROGRESS)
                    .priority(Priority.HIGH)
                    .build();

            Task task3 = Task.builder()
                    .title("Add input validation")
                    .description("Use Jakarta Validation annotations to validate request bodies and return meaningful error messages.")
                    .status(TaskStatus.PENDING)
                    .priority(Priority.MEDIUM)
                    .build();

            Task task4 = Task.builder()
                    .title("Write unit tests")
                    .description("Create tests for the service layer using JUnit 5 and Mockito.")
                    .status(TaskStatus.PENDING)
                    .priority(Priority.MEDIUM)
                    .build();

            Task task5 = Task.builder()
                    .title("Write project documentation")
                    .description("Create a professional README.md with API documentation and setup instructions.")
                    .status(TaskStatus.PENDING)
                    .priority(Priority.LOW)
                    .build();

            taskRepository.save(task1);
            taskRepository.save(task2);
            taskRepository.save(task3);
            taskRepository.save(task4);
            taskRepository.save(task5);

            System.out.println("✅ Sample data loaded: " + taskRepository.count() + " tasks created.");
        }
    }
}
