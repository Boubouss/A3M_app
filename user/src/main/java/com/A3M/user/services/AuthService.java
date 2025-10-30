package com.A3M.user.services;

import com.A3M.user.dto.user.request.UserLoginDto;
import com.A3M.user.dto.user.request.UserRegistrationDto;
import com.A3M.user.dto.user.response.UserDto;
import com.A3M.user.dto.user.response.UserLoggedDto;
import com.A3M.user.enums.ERole;
import com.A3M.user.exception.type.AlreadyUsedException;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserLoggedDto registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AlreadyUsedException("This email is already used");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new AlreadyUsedException("This username is already used");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        String token = jwtService.generateToken(authentication);

        return UserLoggedDto.builder()
                .token(token)
                .user(UserDto.from(user))
                .type("Bearer")
                .build();
    }

    public UserLoggedDto loginUser(UserLoginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        String token = jwtService.generateToken(authentication);

        User user = (User) authentication.getPrincipal();

        return UserLoggedDto.builder()
                .token(token)
                .user(UserDto.from(user))
                .type("Bearer")
                .build();
    }
}