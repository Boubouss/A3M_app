package com.A3M.user.controller;

import com.A3M.user.enums.ERole;
import com.A3M.user.model.Athlete;
import com.A3M.user.model.User;
import com.A3M.user.repository.AthleteRepository;
import com.A3M.user.repository.UserRepository;
import com.A3M.user.security.WithMockCustomUser;
import com.A3M.user.services.AthleteService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AthleteControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AthleteService athleteService;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("Jane");
        user.setEmail("test1@test.com");
        user.setPhoneNumber("0123456789");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setRole(ERole.ROLE_USER);

        Athlete athlete = new Athlete();
        athlete.setFirstName("John");
        athlete.setLastName("Does");
        athlete.setGender(true);
        athlete.setBirthDate(LocalDate.now());
        athlete.setUser(user);

        Athlete athlete2 = new Athlete();
        athlete2.setFirstName("Jane");
        athlete2.setLastName("Does");
        athlete2.setGender(false);
        athlete2.setBirthDate(LocalDate.now());
        athlete2.setUser(user);

        user.setAthletes(Set.of(athlete, athlete2));

        userRepository.save(user);
        athleteRepository.save(athlete);
    }

    @AfterEach
    void tearDown() {
        //userRepository.deleteAll();
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "ADMIN")
    void all_Success() throws Exception {
        mockMvc.perform(get("/api/athlete/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"));
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    void all_Failure() throws Exception {
        mockMvc.perform(get("/api/athlete/all"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    void create_Success() throws Exception {
        mockMvc.perform(post("/api/athlete/create").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Redwane",
                        "lastName": "Bouselham",
                        "gender": "true",
                        "birthDate": "1996-08-30"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Redwane"));
    }

    @Test
    void create_Failure() throws Exception {
        mockMvc.perform(post("/api/athlete/create").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Redwane",
                        "lastName": "Bouselham",
                        "gender": "true",
                        "birthDate": "1996-08-30"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    void create_Badrequest() throws Exception {
        mockMvc.perform(post("/api/athlete/create").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "lastName": "Bouselham",
                        "gender": "true",
                        "birthDate": "1996-08-30"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    void update_Success() throws Exception {
        mockMvc.perform(post("/api/athlete/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Redwane"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Redwane"));
    }

    @Test
    @WithMockCustomUser(username = "John", roles = "USER")
    void update_NotFound() throws Exception {
        mockMvc.perform(post("/api/athlete/update/9").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Redwane",
                        "lastName": "Bouselham",
                        "gender": "true",
                        "birthDate": "1996-08-30"
                    }
                    """))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
