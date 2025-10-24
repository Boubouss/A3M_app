package com.A3M.user.services;

import com.A3M.user.dto.user.request.UserLoginDto;
import com.A3M.user.dto.user.request.UserRegistrationDto;
import com.A3M.user.dto.user.response.UserDto;
import com.A3M.user.dto.user.response.UserLoggedDto;
import com.A3M.user.enums.ERole;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail()) || userRepository.existsByUsername(dto.getUsername())) {
            // throw new Exception
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(ERole.ROLE_USER);

        return userRepository.save(user);
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
                .build();
    }
}