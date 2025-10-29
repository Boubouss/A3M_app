package com.A3M.user.services;

import com.A3M.user.dto.user.request.UserSuperUpdateDto;
import com.A3M.user.dto.user.request.UserUpdateDto;
import com.A3M.user.dto.user.response.UserDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;


    public List<UserDto> getAllUsers() {
        List<UserDto> response = new ArrayList<>();
        Iterable<User> users = repository.findAll();
        for (User user : users) {
            UserDto dto = UserDto.from(user);
            response.add(dto);
        }
        return response;
    }

    @Transactional
    public UserDto updateUser(UserUpdateDto dto, User user) {
        User updatedUser = user.toBuilder()
                .email(dto.getEmail() != null ? dto.getEmail() : user.getEmail())
                .phoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : user.getPhoneNumber())
                .username(dto.getUsername() != null ? dto.getUsername() : user.getUsername())
                .build();

        if (dto.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repository.save(updatedUser);
        return UserDto.from(updatedUser);
    }

    @Transactional
    public UserDto updateThisUser(Long id, UserSuperUpdateDto dto) {
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        User userUpdated = user.toBuilder()
                .email(dto.getEmail() != null ? dto.getEmail() : user.getEmail())
                .phoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : user.getPhoneNumber())
                .username(dto.getUsername() != null ? dto.getUsername() : user.getUsername())
                .role(dto.getRole() != null ? dto.getRole() : user.getRole())
                .build();

        if (dto.getPassword() != null) {
            userUpdated.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repository.save(userUpdated);
        return UserDto.from(userUpdated);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
