package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAllTasks();
    List<Task> getTasksByUserId(Long userId);
    List<Task> getTasksByUser(User user);
    Optional<Task> getTaskById(Long id);
    Task createTask(Task task);
    Task updateTask(Long id, Task taskDetails);
    void deleteTask(Long id);
    List<Task> getTasksByStatus(Task.Status status);
    List<Task> getTasksByUserIdAndStatus(Long userId, Task.Status status);
}