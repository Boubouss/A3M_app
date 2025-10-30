package com.A3M.user.service;

import com.A3M.user.dto.athlete.request.AthleteCreationDto;
import com.A3M.user.dto.athlete.request.AthleteUpdateDto;
import com.A3M.user.dto.athlete.response.AthleteDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.Athlete;
import com.A3M.user.model.User;
import com.A3M.user.repository.AthleteRepository;
import com.A3M.user.repository.UserRepository;
import com.A3M.user.services.AthleteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AthleteServiceTest {
    @Mock
    private AthleteRepository repository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AthleteService athleteService;

    @Test
    public void createAthlete_Success() {
        AthleteCreationDto dto = new AthleteCreationDto();
        User user = new User();
        user.setId(1L);

        when(repository.save(any(Athlete.class))).then(invocation -> {
            Athlete athlete = invocation.getArgument(0);
            athlete.setId(1L);
            return athlete;
        });

        AthleteDto result = athleteService.createAthlete(user, dto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        assertEquals(1, user.getAthletes().size());
        assertEquals(user.getAthletes().stream().toList().getFirst().getId(), result.getId());
        verify(repository, times(1)).save(any(Athlete.class));
    }


    @Test
    public void updateAthlete_Success() {
        AthleteUpdateDto dto = new AthleteUpdateDto();
        dto.setFirstName("Redwane");

        User user = new User();
        user.setId(1L);

        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setFirstName("Jawede");
        athlete.setLastName("Bouselham");
        athlete.setUser(user);
        user.setAthletes(Set.of(athlete));

        when(repository.findById(any())).thenReturn(Optional.of(athlete));
        when(repository.save(any(Athlete.class))).then(invocation -> invocation.getArgument(0));

        AthleteDto result = athleteService.updateAthlete(user, 1L, dto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        assertEquals(1, user.getAthletes().size());
        assertEquals(user.getAthletes().stream().toList().getFirst().getId(), result.getId());
        assertEquals("Redwane", result.getFirstName());
        assertEquals("Bouselham", result.getLastName());
        verify(repository, times(1)).save(any(Athlete.class));
    }

    @Test
    public void updateAthlete_NotFound() {
        AthleteUpdateDto dto = new AthleteUpdateDto();
        dto.setFirstName("Redwane");

        User user = new User();
        user.setId(1L);

        Athlete athlete = new Athlete();
        athlete.setId(2L);

        when(repository.findById(any())).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> athleteService.updateAthlete(user, 1L, dto));
    }

    @Test
    public void updateAthlete_NotContains() {
        AthleteUpdateDto dto = new AthleteUpdateDto();
        dto.setFirstName("Redwane");

        User user = new User();
        user.setId(1L);

        Athlete athlete = new Athlete();
        athlete.setId(1L);

        when(repository.findById(any())).thenReturn(Optional.of(athlete));

        assertThrows(UserNotFoundException.class, () -> athleteService.updateAthlete(user, 1L, dto));
    }

    @Test
    public void deleteOneAthlete_Success() {
        User user = new User();
        user.setId(1L);

        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setUser(user);
        user.setAthletes(Set.of(athlete));

        when(repository.findById(any())).thenReturn(Optional.of(athlete));

        athleteService.deleteAthlete(user, 1L);

        assertEquals(0, user.getAthletes().size());
        verify(repository, times(1)).delete(athlete);
    }

    @Test
    public void deleteAthlete_Success() {
        User user = new User();
        user.setId(1L);

        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setUser(user);
        Athlete athlete2 = new Athlete();
        athlete2.setId(2L);
        athlete2.setUser(user);
        user.setAthletes(Set.of(athlete,  athlete2));

        when(repository.findById(any())).thenReturn(Optional.of(athlete));

        athleteService.deleteAthlete(user, 1L);

        assertEquals(1, user.getAthletes().size());
        assertEquals(user.getAthletes().stream().toList().getFirst().getId(), athlete2.getId());
        verify(repository, times(1)).delete(athlete);
    }
}
