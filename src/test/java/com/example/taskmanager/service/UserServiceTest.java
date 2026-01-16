package com.example.taskmanager.service;

import com.example.taskmanager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void shouldCreateAndRetrieveUser() {
        // Given
        User user = new User("Test User", "test@example.com");

        // When
        User createdUser = userService.createUser(user);
        Optional<User> retrievedUser = userService.getUserById(createdUser.getId());

        // Then
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getName()).isEqualTo("Test User");
        assertThat(retrievedUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldReturnAllUsers() {
        // Given
        User user1 = new User("User 1", "user1@example.com");
        User user2 = new User("User 2", "user2@example.com");
        userService.createUser(user1);
        userService.createUser(user2);

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertThat(users).hasSize(2);
    }

    @Test
    void shouldUpdateUser() {
        // Given
        User user = new User("Original Name", "original@example.com");
        User createdUser = userService.createUser(user);

        // When
        User updateUser = new User();
        updateUser.setName("Updated Name");
        updateUser.setEmail("updated@example.com");
        User updatedUser = userService.updateUser(createdUser.getId(), updateUser);

        // Then
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void shouldDeleteUser() {
        // Given
        User user = new User("To Delete", "delete@example.com");
        User createdUser = userService.createUser(user);

        // When
        userService.deleteUser(createdUser.getId());
        Optional<User> deletedUser = userService.getUserById(createdUser.getId());

        // Then
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {
        // Given
        User user = new User("Check Email", "check@example.com");
        userService.createUser(user);

        // When
        boolean exists = userService.existsByEmail("check@example.com");

        // Then
        assertThat(exists).isTrue();
    }
}