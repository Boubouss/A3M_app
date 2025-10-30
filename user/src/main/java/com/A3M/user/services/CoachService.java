package com.A3M.user.services;

import com.A3M.user.dto.chaperone.request.ChaperoneCreationDto;
import com.A3M.user.dto.coach.request.CoachCreationDto;
import com.A3M.user.dto.coach.request.CoachUpdateDto;
import com.A3M.user.dto.coach.response.CoachDto;
import com.A3M.user.exception.type.AlreadyUsedException;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.Coach;
import com.A3M.user.model.User;
import com.A3M.user.repository.CoachRepository;
import com.A3M.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoachService {
    private final CoachRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public List<CoachDto> getAllCoach() {
        List<Coach> coachList = repository.findAll();
        List<CoachDto> coachDtoList = new ArrayList<>();
        for (Coach coach : coachList) {
            CoachDto coachDto = CoachDto.from(coach);
            coachDtoList.add(coachDto);
        }
        return coachDtoList;
    }

    @Transactional
    public CoachDto createCoach(User user, CoachCreationDto dto) {
        if (user.getCoach() != null) {
            throw new AlreadyUsedException("Coach already exist");
        }
        Coach coach = dto.generate();
        coach.setUser(user);
        user.setCoach(coach);
        return CoachDto.from(repository.save(coach));
    }

    @Transactional
    public CoachDto updateCoach(User user, CoachUpdateDto dto) {
        Coach coach = user.getCoach().toBuilder()
                .firstName(dto.getFirstName() != null ? dto.getFirstName() : user.getCoach().getFirstName())
                .lastName(dto.getLastName() != null ? dto.getLastName() : user.getCoach().getLastName())
                .build();
        return CoachDto.from(repository.save(coach));
    }

    @Transactional
    public void deleteCoach(User user) {
        Coach coach = user.getCoach();
        if (coach == null) {
            throw new UserNotFoundException("Coach not found");
        }
        repository.delete(coach);
        user.setCoach(null);
        userRepository.save(user);
    }
}
