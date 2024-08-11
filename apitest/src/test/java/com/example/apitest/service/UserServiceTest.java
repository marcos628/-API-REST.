package com.example.apitest.service;


import com.example.apitest.model.User;
import com.example.apitest.repository.UserRepository;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_Found() {
        User mockUser = new User(1L, "John Doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User user = userService.getUserById(1L);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User user = userService.getUserById(1L);

        assertNull(user);
    }

    @Test
    public void testCreateUser() {
        User userToSave = new User(null, "Jane Doe");
        User savedUser = new User(2L, "Jane Doe");
        when(userRepository.save(userToSave)).thenReturn(savedUser);

        User user = userService.createUser(userToSave);

        assertNotNull(user);
        assertEquals(2L, user.getId());
        assertEquals("Jane Doe", user.getName());
    }

    @Test
    public void testUpdateUser_Success() {
        User existingUser = new User(1L, "John Smith");
        User updatedUser = new User(1L, "John Doe");
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User user = userService.updateUser(1L, updatedUser);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testUpdateUser_NotFound() {
        User updatedUser = new User(1L, "John Doe");
        when(userRepository.existsById(1L)).thenReturn(false);

        User user = userService.updateUser(1L, updatedUser);

        assertNull(user);
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User(1L, "Alice");
        User user2 = new User(2L, "Bob");
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
    }
}
