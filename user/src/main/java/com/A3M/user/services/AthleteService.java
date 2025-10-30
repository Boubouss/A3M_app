package com.A3M.user.services;

import com.A3M.user.dto.athlete.request.AthleteCreationDto;
import com.A3M.user.dto.athlete.request.AthleteSearchAgeDto;
import com.A3M.user.dto.athlete.request.AthleteSearchNameDto;
import com.A3M.user.dto.athlete.request.AthleteUpdateDto;
import com.A3M.user.dto.athlete.response.AthleteDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.Athlete;
import com.A3M.user.model.User;
import com.A3M.user.repository.AthleteRepository;
import com.A3M.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AthleteService {
    private final AthleteRepository repository;

    private final UserRepository userRepository;

    @Transactional
    public AthleteDto createAthlete(User user, AthleteCreationDto dto) {
        Athlete athlete = dto.generate();
        user.getAthletes().add(athlete);
        athlete.setUser(user);
        return AthleteDto.from(repository.save(athlete));
    }

    @Transactional
    public AthleteDto updateAthlete(User user, Long id, AthleteUpdateDto dto) {
        Athlete athlete = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getAthletes().contains(athlete)) {
            throw new UserNotFoundException("Athlete not found");
        }
        Athlete athleteUpdated = athlete.toBuilder()
                .birthDate(dto.getBirthDate() != null ? dto.getBirthDate() : athlete.getBirthDate())
                .gender(dto.getGender() != null ? dto.getGender() : athlete.getGender())
                .firstName(dto.getFirstName() != null ? dto.getFirstName() : athlete.getFirstName())
                .lastName(dto.getLastName() != null ? dto.getLastName() : athlete.getLastName())
                .build();

        return AthleteDto.from(repository.save(athleteUpdated));
    }

    @Transactional
    public void deleteAthlete(User user, Long id) {
        Athlete athlete = repository.findById(id).orElseThrow(() -> new UserNotFoundException("Athlete not found"));
        if (!user.getAthletes().contains(athlete)) {
            throw new UserNotFoundException("Athlete not found");
        }
        Set<Athlete> athletes = new HashSet<>(user.getAthletes());
        athletes.remove(athlete);
        user.setAthletes(athletes);
        userRepository.save(user);
        repository.delete(athlete);
    }

    @Transactional
    public List<AthleteDto> getAllAthletes() {
        List<AthleteDto> response = new ArrayList<>();
        Iterable<Athlete> athletes = repository.findAll();
        for (Athlete athlete : athletes) {
            AthleteDto dto = AthleteDto.from(athlete);
            response.add(dto);
        }
        return response;
    }

    @Transactional
    public List<AthleteDto> getAthleteByName(AthleteSearchNameDto dto) {

        List<AthleteDto> response = new ArrayList<>();

        if (dto.getFirstName() != null) {
            Iterable<Athlete> firstNames = repository.findByFirstNameStartingWithOrContaining(dto.getFirstName());
            for (Athlete f : firstNames) {
                AthleteDto d = AthleteDto.from(f);
                response.add(d);
            }
        }

        if (dto.getLastName() != null) {
            Iterable<Athlete> lastNames = repository.findByLastNameStartingWithOrContaining(dto.getLastName());
            for (Athlete f : lastNames) {
                AthleteDto d = AthleteDto.from(f);
                response.add(d);
            }
        }

        return response;
    }

    @Transactional
    public List<AthleteDto> getAthleteByAge(AthleteSearchAgeDto dto) {

        List<AthleteDto> response = new ArrayList<>();

        Iterable<Athlete> athletes = repository.findByAgeBetween(dto.getMinAge(), dto.getMaxAge());
        for (Athlete athlete : athletes) {
            AthleteDto d = AthleteDto.from(athlete);
            response.add(d);
        }

        return response;
    }

}
