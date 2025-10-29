package com.A3M.user.controller;

import com.A3M.user.BaseIntegrationTest;
import com.A3M.user.enums.ERole;
import com.A3M.user.model.User;
import com.A3M.user.repository.UserRepository;
import com.A3M.user.security.WithMockCustomUser;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@Rollback
public class UserControllerIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("John");
        user.setEmail("test@test.com");
        user.setPhoneNumber("0123456789");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.getCache().evictAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    public void me_Success() throws Exception {

        mockMvc.perform(get("/api/user/me"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John"));
    }

    @Test
    public void me_Fail() throws Exception {

        mockMvc.perform(get("/api/user/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "ADMIN")
    public void all_Success() throws Exception {

        mockMvc.perform(get("/api/user/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].username").value("John"));
    }

    @Test
    public void all_Fail() throws Exception {

        mockMvc.perform(get("/api/user/all"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    public void update_Success() throws Exception {

        mockMvc.perform(post("/api/user/update").contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "username": "Jane"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Jane"));

    }

    @Test
    public void update_Fail() throws Exception {

        mockMvc.perform(post("/api/user/update").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "Jane"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    public void update_InvalidDTO() throws Exception {

        mockMvc.perform(post("/api/user/update").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "J"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(username = "Jack", roles = "ADMIN")
    public void updateThis_Success() throws Exception {

        mockMvc.perform(post("/api/user/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "email": "test3@test.com"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test3@test.com"));

    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    public void updateThis_Fail() throws Exception {

        mockMvc.perform(post("/api/user/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "Jane"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "ADMIN")
    public void updateThis_InvalidDTO() throws Exception {

        mockMvc.perform(post("/api/user/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "username": "J"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "ADMIN")
    public void delete_Success() throws Exception {

        mockMvc.perform(delete("/api/user/delete/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    public void delete_Fail() throws Exception {

        mockMvc.perform(delete("/api/user/delete/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
