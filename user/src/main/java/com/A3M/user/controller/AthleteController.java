package com.A3M.user.controller;

import com.A3M.user.dto.athlete.request.AthleteCreationDto;
import com.A3M.user.dto.athlete.request.AthleteSearchAgeDto;
import com.A3M.user.dto.athlete.request.AthleteSearchNameDto;
import com.A3M.user.dto.athlete.request.AthleteUpdateDto;
import com.A3M.user.dto.athlete.response.AthleteDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.User;
import com.A3M.user.services.AthleteService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/athlete")
public class AthleteController {

    @Resource
    private AthleteService athleteService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'STAFF')")
    public ResponseEntity<List<AthleteDto>> all() {
        return ResponseEntity.ok(athleteService.getAllAthletes());
    }

    @PostMapping("/search/name")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'STAFF')")
    public ResponseEntity<List<AthleteDto>> searchByName(@Valid @RequestBody AthleteSearchNameDto dto) {
        if (dto == null) {
            return ResponseEntity.ok(athleteService.getAllAthletes());
        }
        return ResponseEntity.ok(athleteService.getAthleteByName(dto));
    }

    @PostMapping("/search/age")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'STAFF')")
    public ResponseEntity<List<AthleteDto>> searchByAge(@Valid @RequestBody AthleteSearchAgeDto dto) {
        if (dto == null) {
            return ResponseEntity.ok(athleteService.getAllAthletes());
        }
        return ResponseEntity.ok(athleteService.getAthleteByAge(dto));
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AthleteDto> create(@AuthenticationPrincipal User user, @Valid @RequestBody AthleteCreationDto dto) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(athleteService.createAthlete(user, dto));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AthleteDto> update(@AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody AthleteUpdateDto dto) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(athleteService.updateAthlete(user, id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        athleteService.deleteAthlete(user, id);
        return ResponseEntity.ok().build();
    }
}
