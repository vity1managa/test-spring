package com.example.taskmanager.service.impl;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
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

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllUsers() {
        // Given
        User user1 = new User("User 1", "user1@example.com");
        User user2 = new User("User 2", "user2@example.com");
        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> actualUsers = userService.getAllUsers();

        // Then
        assertThat(actualUsers).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUserById() {
        // Given
        Long userId = 1L;
        User expectedUser = new User("Test User", "test@example.com");
        expectedUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // When
        Optional<User> actualUser = userService.getUserById(userId);

        // Then
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getName()).isEqualTo("Test User");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldCreateUser() {
        // Given
        User newUser = new User("New User", "newuser@example.com");
        User savedUser = new User("New User", "newuser@example.com");
        savedUser.setId(1L);
        when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.createUser(newUser);

        // Then
        assertThat(result.getName()).isEqualTo("New User");
        assertThat(result.getEmail()).isEqualTo("newuser@example.com");
        verify(userRepository, times(1)).existsByEmail(newUser.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateUser() {
        // Given
        User duplicateUser = new User("Duplicate User", "duplicate@example.com");
        when(userRepository.existsByEmail(duplicateUser.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            userService.createUser(duplicateUser);
        });
        verify(userRepository, times(1)).existsByEmail(duplicateUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateUser() {
        // Given
        Long userId = 1L;
        User existingUser = new User("Old Name", "old@example.com");
        existingUser.setId(userId);
        User userDetails = new User("Updated Name", "updated@example.com");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(userDetails);

        // When
        User updatedUser = userService.updateUser(userId, userDetails);

        // Then
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        // Given
        Long userId = 1L;
        User existingUser = new User("To Delete", "delete@example.com");
        existingUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(eq(existingUser));
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {
        // Given
        String email = "check@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean exists = userService.existsByEmail(email);

        // Then
        assertThat(exists).isTrue();
        verify(userRepository, times(1)).existsByEmail(email);
    }
}