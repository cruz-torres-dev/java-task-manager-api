package com.portfolio.taskmanager;

import com.portfolio.taskmanager.dto.TaskRequest;
import com.portfolio.taskmanager.dto.TaskResponse;
import com.portfolio.taskmanager.exception.TaskNotFoundException;
import com.portfolio.taskmanager.model.Priority;
import com.portfolio.taskmanager.model.Task;
import com.portfolio.taskmanager.model.TaskStatus;
import com.portfolio.taskmanager.repository.TaskRepository;
import com.portfolio.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TaskServiceImpl class.
 *
 * KEY TESTING CONCEPTS:
 * - @ExtendWith(MockitoExtension.class): Enables Mockito annotations in JUnit 5.
 * - @Mock: Creates a fake (mock) version of TaskRepository.
 *   This means we don't need a real database to run tests.
 * - @InjectMocks: Creates a real TaskServiceImpl but injects the mocked repository.
 * - We test the service in ISOLATION — verifying business logic without
 *   involving the database, controller, or Spring context.
 *
 * TEST NAMING CONVENTION: methodName_WhenCondition_ShouldExpectedBehavior
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task sampleTask;
    private TaskRequest sampleRequest;

    /**
     * Set up test data before each test method runs.
     * This ensures each test starts with fresh, predictable data.
     */
    @BeforeEach
    void setUp() {
        sampleTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("This is a test task")
                .status(TaskStatus.PENDING)
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleRequest = TaskRequest.builder()
                .title("Test Task")
                .description("This is a test task")
                .priority(Priority.MEDIUM)
                .build();
    }

    // ========================================================================
    // CREATE TASK TESTS
    // ========================================================================

    @Test
    @DisplayName("Should create a task successfully")
    void createTask_WithValidRequest_ShouldReturnCreatedTask() {
        // ARRANGE: Define what the mock repository should return
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        // ACT: Call the method under test
        TaskResponse response = taskService.createTask(sampleRequest);

        // ASSERT: Verify the result
        assertNotNull(response);
        assertEquals("Test Task", response.getTitle());
        assertEquals(TaskStatus.PENDING, response.getStatus());
        assertEquals(Priority.MEDIUM, response.getPriority());

        // VERIFY: Ensure save() was called exactly once
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should default to MEDIUM priority when not specified")
    void createTask_WithNoPriority_ShouldDefaultToMedium() {
        TaskRequest noPriorityRequest = TaskRequest.builder()
                .title("No Priority Task")
                .description("Priority should default to MEDIUM")
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        TaskResponse response = taskService.createTask(noPriorityRequest);

        assertNotNull(response);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    // ========================================================================
    // GET ALL TASKS TESTS
    // ========================================================================

    @Test
    @DisplayName("Should return all tasks ordered by date")
    void getAllTasks_ShouldReturnAllTasks() {
        Task task2 = Task.builder()
                .id(2L)
                .title("Second Task")
                .status(TaskStatus.COMPLETED)
                .priority(Priority.HIGH)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(sampleTask, task2));

        List<TaskResponse> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    // ========================================================================
    // GET TASK BY ID TESTS
    // ========================================================================

    @Test
    @DisplayName("Should return task when ID exists")
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        TaskResponse response = taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Task", response.getTitle());
    }

    @Test
    @DisplayName("Should throw exception when ID does not exist")
    void getTaskById_WhenTaskNotFound_ShouldThrowException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    // ========================================================================
    // UPDATE TASK TESTS
    // ========================================================================

    @Test
    @DisplayName("Should update task successfully")
    void updateTask_WhenTaskExists_ShouldReturnUpdatedTask() {
        TaskRequest updateRequest = TaskRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .priority(Priority.HIGH)
                .build();

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .status(TaskStatus.PENDING)
                .priority(Priority.HIGH)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponse response = taskService.updateTask(1L, updateRequest);

        assertEquals("Updated Title", response.getTitle());
        assertEquals(Priority.HIGH, response.getPriority());
    }

    // ========================================================================
    // DELETE TASK TESTS
    // ========================================================================

    @Test
    @DisplayName("Should delete task when ID exists")
    void deleteTask_WhenTaskExists_ShouldDeleteSuccessfully() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent task")
    void deleteTask_WhenTaskNotFound_ShouldThrowException() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(99L));
        verify(taskRepository, never()).deleteById(99L);
    }

    // ========================================================================
    // MARK AS COMPLETED TESTS
    // ========================================================================

    @Test
    @DisplayName("Should mark task as completed")
    void markTaskAsCompleted_WhenTaskExists_ShouldSetStatusCompleted() {
        Task completedTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .status(TaskStatus.COMPLETED)
                .priority(Priority.MEDIUM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(completedTask);

        TaskResponse response = taskService.markTaskAsCompleted(1L);

        assertEquals(TaskStatus.COMPLETED, response.getStatus());
    }

    // ========================================================================
    // GET TASKS BY STATUS TESTS
    // ========================================================================

    @Test
    @DisplayName("Should filter tasks by status")
    void getTasksByStatus_WithValidStatus_ShouldReturnFilteredTasks() {
        when(taskRepository.findByStatus(TaskStatus.PENDING))
                .thenReturn(List.of(sampleTask));

        List<TaskResponse> tasks = taskService.getTasksByStatus("PENDING");

        assertEquals(1, tasks.size());
        assertEquals(TaskStatus.PENDING, tasks.get(0).getStatus());
    }

    @Test
    @DisplayName("Should throw exception for invalid status")
    void getTasksByStatus_WithInvalidStatus_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> taskService.getTasksByStatus("INVALID"));
    }
}
