package com.A3M.user.service;

import com.A3M.user.dto.user.request.UserSuperUpdateDto;
import com.A3M.user.dto.user.request.UserUpdateDto;
import com.A3M.user.dto.user.response.UserDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import com.A3M.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsers_Success() {

        User user = new User();
        user.setUsername("John");
        user.setEmail("test@example.com");
        user.setPhoneNumber("0123456789");

        List<User> users = List.of(user);

        when(repository.findAll()).thenReturn(users);

        List<UserDto> userList = userService.getAllUsers();

        assertNotNull(userList);
        assertEquals(1, userList.size());
        assertEquals("John", userList.getFirst().getUsername());
        assertEquals("test@example.com", userList.getFirst().getEmail());
        assertEquals("0123456789", userList.getFirst().getPhoneNumber());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void getAllUsers_Empty() {
        List<User> users = List.of();

        when(repository.findAll()).thenReturn(users);

        List<UserDto> userList = userService.getAllUsers();

        assertNotNull(userList);
        assertEquals(0, userList.size());
        assertTrue(userList.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void updateUser_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("John");
        user.setEmail("test@example.com");

        UserUpdateDto dto = new UserUpdateDto();
        dto.setUsername("Jane");
        dto.setPassword("Password123!");

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("EncodedPassword123!");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updated = userService.updateUser(dto, user);

        assertNotNull(updated);
        assertEquals("Jane", updated.getUsername());
        assertEquals("test@example.com", updated.getEmail());
        verify(repository, times(1)).save(any(User.class));

    }

    @Test
    public void updateThisUser_Success() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("John");
        existingUser.setEmail("test@example.com");

        UserSuperUpdateDto dto = new UserSuperUpdateDto();
        dto.setUsername("Jane");

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updated = userService.updateThisUser(1L, dto);

        assertNotNull(updated);
        assertEquals("Jane", updated.getUsername());
        assertEquals("test@example.com", updated.getEmail());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    public void updateThisUser_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateThisUser(1L, new UserSuperUpdateDto()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }
}
