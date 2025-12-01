package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.User;
import edu.uoc.epcsd.user.domain.repository.AlertRepository;
import edu.uoc.epcsd.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AlertRepository alertRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, alertRepository);
    }

    @Test
    void authenticateReturnsUserWhenPasswordMatches() {
        User storedUser = buildUser("john.doe@example.com", "secret");
        when(userRepository.findUserByEmail(storedUser.getEmail())).thenReturn(Optional.of(storedUser));

        Optional<User> result = userService.authenticate(storedUser.getEmail(), "secret");

        assertThat(result).contains(storedUser);
    }

    @Test
    void authenticateReturnsEmptyWhenUserNotFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userService.authenticate("missing@example.com", "any");

        assertThat(result).isEmpty();
    }

    @Test
    void authenticateReturnsEmptyWhenPasswordDoesNotMatch() {
        User storedUser = buildUser("john.doe@example.com", "secret");
        when(userRepository.findUserByEmail(storedUser.getEmail())).thenReturn(Optional.of(storedUser));

        Optional<User> result = userService.authenticate(storedUser.getEmail(), "wrong");

        assertThat(result).isEmpty();
    }

    private User buildUser(String email, String password) {
        return User.builder()
                .id(1L)
                .email(email)
                .fullName("John Doe")
                .password(password)
                .phoneNumber("555-1234")
                .build();
    }
}
