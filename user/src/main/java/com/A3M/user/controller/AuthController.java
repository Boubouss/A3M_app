package com.A3M.user.controller;

import com.A3M.user.dto.user.response.UserDto;
import com.A3M.user.dto.user.request.UserLoginDto;
import com.A3M.user.dto.user.request.UserRegistrationDto;
import com.A3M.user.dto.user.response.UserLoggedDto;
import com.A3M.user.model.User;
import com.A3M.user.services.AuthService;
import com.A3M.user.services.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto dto) {
        User user = authService.registerUser(dto);
        return ResponseEntity.ok(UserDto.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoggedDto> login(@Valid @RequestBody UserLoginDto dto) {
        return ResponseEntity.ok(authService.loginUser(dto));
    }
}
