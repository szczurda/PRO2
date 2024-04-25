package com.example.pro2.tests;

import com.example.pro2.entities.User;
import com.example.pro2.repositories.UserRepository;
import com.example.pro2.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class UserRepoTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User("testpassword", "testuser", "USER");
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void shouldReturnUserByUsername() {
        String username = "testuser";
        User user = new User("encodedPassword", username, "USER");
        when(userRepository.findByUsername(username)).thenReturn(user);

        User retrievedUser = userService.getUserByUsername(username);

        assertEquals(user, retrievedUser);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    public void shouldThrowExceptionIfUserNotFound() {
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserByUsername(username);
        });


    }


}
