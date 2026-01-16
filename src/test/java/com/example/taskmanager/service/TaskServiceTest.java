package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateAndRetrieveTask() {
        // Given
        User user = new User("Test User", "test@example.com");
        User savedUser = userService.createUser(user);
        
        Task task = new Task("Test Task", "Test Description", savedUser);

        // When
        Task createdTask = taskService.createTask(task);
        Optional<Task> retrievedTask = taskService.getTaskById(createdTask.getId());

        // Then
        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get().getTitle()).isEqualTo("Test Task");
        assertThat(retrievedTask.get().getUser().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void shouldReturnAllTasks() {
        // Given
        User user = new User("Test User", "test@example.com");
        User savedUser = userService.createUser(user);
        
        Task task1 = new Task("Task 1", "Description 1", savedUser);
        Task task2 = new Task("Task 2", "Description 2", savedUser);
        taskService.createTask(task1);
        taskService.createTask(task2);

        // When
        List<Task> tasks = taskService.getAllTasks();

        // Then
        assertThat(tasks).hasSize(2);
    }

    @Test
    void shouldUpdateTask() {
        // Given
        User user = new User("Test User", "test@example.com");
        User savedUser = userService.createUser(user);
        
        Task task = new Task("Original Task", "Original Description", savedUser);
        Task createdTask = taskService.createTask(task);

        // When
        Task updateTask = new Task();
        updateTask.setTitle("Updated Task");
        updateTask.setDescription("Updated Description");
        updateTask.setStatus(Task.Status.IN_PROGRESS);
        updateTask.setUser(savedUser);
        Task updatedTask = taskService.updateTask(createdTask.getId(), updateTask);

        // Then
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Task");
        assertThat(updatedTask.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedTask.getStatus()).isEqualTo(Task.Status.IN_PROGRESS);
    }

    @Test
    void shouldDeleteTask() {
        // Given
        User user = new User("Test User", "test@example.com");
        User savedUser = userService.createUser(user);
        
        Task task = new Task("To Delete", "Description", savedUser);
        Task createdTask = taskService.createTask(task);

        // When
        taskService.deleteTask(createdTask.getId());
        Optional<Task> deletedTask = taskService.getTaskById(createdTask.getId());

        // Then
        assertThat(deletedTask).isEmpty();
    }

    @Test
    void shouldFindTasksByUserId() {
        // Given
        User user = new User("Test User", "test@example.com");
        User savedUser = userService.createUser(user);
        
        Task task1 = new Task("Task 1", "Description 1", savedUser);
        Task task2 = new Task("Task 2", "Description 2", savedUser);
        taskService.createTask(task1);
        taskService.createTask(task2);

        // When
        List<Task> userTasks = taskService.getTasksByUserId(savedUser.getId());

        // Then
        assertThat(userTasks).hasSize(2);
    }
}