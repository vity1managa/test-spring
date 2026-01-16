package com.example.taskmanager.service;

import com.example.taskmanager.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
}