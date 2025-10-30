package com.A3M.user.services;

import com.A3M.user.dto.chaperone.request.ChaperoneCreationDto;
import com.A3M.user.dto.chaperone.request.ChaperoneSearchDto;
import com.A3M.user.dto.chaperone.request.ChaperoneUpdateDto;
import com.A3M.user.dto.chaperone.response.ChaperoneDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.Chaperone;
import com.A3M.user.model.User;
import com.A3M.user.repository.ChaperoneRepository;
import com.A3M.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChaperoneService {
    private final ChaperoneRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public List<ChaperoneDto> getAllChaperones() {
        List<Chaperone> chaperoneList = repository.findAll();
        List<ChaperoneDto> response = new ArrayList<>();
        for (Chaperone chaperone : chaperoneList) {
            ChaperoneDto dto = ChaperoneDto.from(chaperone);
            response.add(dto);
        }
        return response;
    }

    @Transactional
    public ChaperoneDto createChaperone(User user, ChaperoneCreationDto dto) {
        Chaperone chaperone = dto.generate();
        user.getChaperones().add(chaperone);
        chaperone.setUser(user);
        return ChaperoneDto.from(repository.save(chaperone));
    }

    @Transactional
    public ChaperoneDto updateChaperone(User user, Long id, ChaperoneUpdateDto dto) {
        Chaperone chaperone =  repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getChaperones().contains(chaperone)) {
            throw new UserNotFoundException("Chaperone not found");
        }
        Chaperone chaperoneUpdated = chaperone.toBuilder()
                .firstName(dto.getFirstName() != null ? dto.getFirstName() : chaperone.getFirstName())
                .lastName(dto.getLastName() != null ? dto.getLastName() : chaperone.getLastName())
                .build();

        return ChaperoneDto.from(repository.save(chaperoneUpdated));
    }

    @Transactional
    public void deleteChaperone(User user, Long id) {
        Chaperone chaperone = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!user.getChaperones().contains(chaperone)) {
            throw new UserNotFoundException("Chaperone not found");
        }
        Set<Chaperone> chaperoneSet = new HashSet<>(user.getChaperones());
        chaperoneSet.remove(chaperone);
        user.setChaperones(chaperoneSet);
        userRepository.save(user);
        repository.delete(chaperone);
    }

    @Transactional
    public List<ChaperoneDto> getChaperoneByName(ChaperoneSearchDto dto) {

        List<ChaperoneDto> response = new ArrayList<>();

        if (dto.getFirstName() != null) {
            Iterable<Chaperone> firstNames = repository.findByFirstNameStartingWithOrContaining(dto.getFirstName());
            for (Chaperone c : firstNames) {
                ChaperoneDto d = ChaperoneDto.from(c);
                response.add(d);
            }
        }

        if (dto.getLastName() != null) {
            Iterable<Chaperone> lastNames = repository.findByLastNameStartingWithOrContaining(dto.getLastName());
            for (Chaperone c : lastNames) {
                ChaperoneDto d = ChaperoneDto.from(c);
                response.add(d);
            }
        }

        return response;
    }
}
