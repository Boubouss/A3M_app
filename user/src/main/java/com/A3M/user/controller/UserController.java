package com.A3M.user.controller;

import com.A3M.user.dto.user.request.UserSuperUpdateDto;
import com.A3M.user.dto.user.request.UserUpdateDto;
import com.A3M.user.dto.user.response.UserDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import com.A3M.user.services.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @Resource
    private UserService userService;

    private final UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(UserDto.from(user));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> all() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> update(@Valid @RequestBody UserUpdateDto dto, @AuthenticationPrincipal User user) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(userService.updateUser(dto, user));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @Valid @RequestBody UserSuperUpdateDto dto) {
        return ResponseEntity.ok(userService.updateThisUser(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

}
