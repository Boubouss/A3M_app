package com.A3M.user.controller;

import com.A3M.user.BaseIntegrationTest;
import com.A3M.user.enums.ERole;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
public class AuthControllerIT extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setUsername("John");
        user.setPhoneNumber("123456789");
        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "John1",
                        "email": "test1@test.com",
                        "password": "Password123!",
                        "phoneNumber": "0123456789"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void registerUser_AlreadyExist() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "John",
                        "email": "test@test.com",
                        "password": "Password123!",
                        "phoneNumber": "0123456789"
                    }
                    """))
                .andExpect(status().isConflict());
    }

    @Test
    void registerUser_InvalidEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "John1",
                        "email": "test",
                        "password": "Password123!",
                        "phoneNumber": "0123456789"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_InvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "John1",
                        "email": "test1@test.com",
                        "password": "Password",
                        "phoneNumber": "0123456789"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginUser_Success() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "John",
                        "password": "Password123!"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void loginUser_Fail() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "John",
                        "password": "Password456!"
                    }
                    """))
                .andExpect(status().isForbidden());
    }
}
