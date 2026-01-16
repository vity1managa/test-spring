package com.example.taskmanager.service.impl;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllTasks() {
        // Given
        User user = new User("Test User", "test@example.com");
        Task task1 = new Task("Task 1", "Description 1", user);
        Task task2 = new Task("Task 2", "Description 2", user);
        List<Task> expectedTasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = taskService.getAllTasks();

        // Then
        assertThat(actualTasks).hasSize(2);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTaskById() {
        // Given
        Long taskId = 1L;
        User user = new User("Test User", "test@example.com");
        Task expectedTask = new Task("Test Task", "Test Description", user);
        expectedTask.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedTask));

        // When
        Optional<Task> actualTask = taskService.getTaskById(taskId);

        // Then
        assertThat(actualTask).isPresent();
        assertThat(actualTask.get().getTitle()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void shouldCreateTask() {
        // Given
        User user = new User("Test User", "test@example.com");
        Task newTask = new Task("New Task", "New Description", user);
        Task savedTask = new Task("New Task", "New Description", user);
        savedTask.setId(1L);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        Task result = taskService.createTask(newTask);

        // Then
        assertThat(result.getTitle()).isEqualTo("New Task");
        assertThat(result.getDescription()).isEqualTo("New Description");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldUpdateTask() {
        // Given
        Long taskId = 1L;
        User user = new User("Test User", "test@example.com");
        Task existingTask = new Task("Old Title", "Old Description", user);
        existingTask.setId(taskId);
        
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Title");
        taskDetails.setDescription("Updated Description");
        taskDetails.setStatus(Task.Status.IN_PROGRESS);
        taskDetails.setUser(user);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(taskDetails);

        // When
        Task updatedTask = taskService.updateTask(taskId, taskDetails);

        // Then
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Title");
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        // Given
        Long taskId = 1L;
        User user = new User("Test User", "test@example.com");
        Task existingTask = new Task("To Delete", "Description", user);
        existingTask.setId(taskId);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // When
        taskService.deleteTask(taskId);

        // Then
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(eq(existingTask));
    }

    @Test
    void shouldGetTasksByUserId() {
        // Given
        Long userId = 1L;
        User user = new User("Test User", "test@example.com");
        user.setId(userId);
        
        Task task1 = new Task("Task 1", "Description 1", user);
        Task task2 = new Task("Task 2", "Description 2", user);
        List<Task> expectedTasks = Arrays.asList(task1, task2);
        
        when(taskRepository.findByUserId(userId)).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = taskService.getTasksByUserId(userId);

        // Then
        assertThat(actualTasks).hasSize(2);
        verify(taskRepository, times(1)).findByUserId(userId);
    }
}