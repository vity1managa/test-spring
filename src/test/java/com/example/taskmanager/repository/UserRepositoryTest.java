package com.example.taskmanager.repository;

import com.example.taskmanager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserById() {
        // Given
        User user = new User("John Doe", "john@example.com");

        // When
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("John Doe");
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void shouldFindByEmail() {
        // Given
        User user = new User("Jane Doe", "jane@example.com");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail("jane@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void shouldCheckIfExistsByEmail() {
        // Given
        User user = new User("Alice Smith", "alice@example.com");
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail("alice@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldNotFindNonExistentEmail() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(foundUser).isEmpty();
    }
}