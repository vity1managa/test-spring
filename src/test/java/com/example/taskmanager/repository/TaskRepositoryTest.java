package com.example.taskmanager.repository;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindTaskById() {
        // Given
        User user = new User("John Doe", "john@example.com");
        User savedUser = userRepository.save(user);
        
        Task task = new Task("Test Task", "Test Description", savedUser);

        // When
        Task savedTask = taskRepository.save(task);
        var foundTask = taskRepository.findById(savedTask.getId());

        // Then
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Test Task");
        assertThat(foundTask.get().getUser().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void shouldFindTasksByUserId() {
        // Given
        User user = new User("Jane Doe", "jane@example.com");
        User savedUser = userRepository.save(user);
        
        Task task1 = new Task("Task 1", "Description 1", savedUser);
        Task task2 = new Task("Task 2", "Description 2", savedUser);
        taskRepository.save(task1);
        taskRepository.save(task2);

        // When
        List<Task> tasks = taskRepository.findByUserId(savedUser.getId());

        // Then
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle).containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void shouldFindTasksByStatus() {
        // Given
        User user = new User("Bob Smith", "bob@example.com");
        User savedUser = userRepository.save(user);
        
        Task pendingTask = new Task("Pending Task", "Description", savedUser);
        pendingTask.setStatus(Task.Status.PENDING);
        
        Task completedTask = new Task("Completed Task", "Description", savedUser);
        completedTask.setStatus(Task.Status.COMPLETED);
        
        taskRepository.save(pendingTask);
        taskRepository.save(completedTask);

        // When
        List<Task> pendingTasks = taskRepository.findByStatus(Task.Status.PENDING);

        // Then
        assertThat(pendingTasks).hasSize(1);
        assertThat(pendingTasks.get(0).getStatus()).isEqualTo(Task.Status.PENDING);
    }
}