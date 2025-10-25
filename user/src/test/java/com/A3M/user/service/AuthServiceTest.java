package com.A3M.user.service;

import com.A3M.user.dto.user.request.UserLoginDto;
import com.A3M.user.dto.user.request.UserRegistrationDto;
import com.A3M.user.dto.user.response.UserLoggedDto;
import com.A3M.user.enums.ERole;
import com.A3M.user.exception.type.AlreadyUsedException;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import com.A3M.user.services.AuthService;
import com.A3M.user.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void registerUser_Success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("username");
        dto.setPassword("password");
        dto.setEmail("email");
        dto.setPhoneNumber("phoneNumber");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("token");
        when(userRepository.save(any(User.class))).then(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserLoggedDto result = authService.registerUser(dto);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getUser());
        assertEquals(dto.getEmail(), result.getUser().getEmail());
        assertEquals(dto.getPhoneNumber(), result.getUser().getPhoneNumber());
        assertEquals(dto.getUsername(), result.getUser().getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void registerUser_EmailAlreadyUsed() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setEmail("email");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(AlreadyUsedException.class, () -> authService.registerUser(dto));
    }

    @Test
    public void registerUser_UsernameAlreadyUsed() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("username");

        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true);

        assertThrows(AlreadyUsedException.class, () -> authService.registerUser(dto));
    }

    @Test
    public void loginUser_Success() {
        UserLoginDto dto = new UserLoginDto();
        dto.setUsername("username");
        dto.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setEmail("email");
        user.setPhoneNumber("phoneNumber");
        user.setPassword("password");
        user.setRole(ERole.ROLE_USER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        when(jwtService.generateToken(any())).thenReturn("fake-jwt-token");

        UserLoggedDto result = authService.loginUser(dto);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getUser());
        assertEquals(dto.getUsername(), result.getUser().getUsername());
        assertEquals(user.getEmail(), result.getUser().getEmail());
        assertEquals(user.getPhoneNumber(), result.getUser().getPhoneNumber());
        assertEquals("fake-jwt-token", result.getToken());
    }

    @Test
    void loginUser_InvalidCredentials() {
        UserLoginDto dto = new UserLoginDto();
        dto.setUsername("username");
        dto.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid email or password") {
                });

        assertThrows(AuthenticationException.class, () -> authService.loginUser(dto));
    }
}
