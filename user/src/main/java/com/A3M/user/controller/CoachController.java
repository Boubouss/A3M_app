package com.A3M.user.controller;

import com.A3M.user.dto.coach.request.CoachCreationDto;
import com.A3M.user.dto.coach.request.CoachUpdateDto;
import com.A3M.user.dto.coach.response.CoachDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.User;
import com.A3M.user.services.CoachService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coach")
public class CoachController {
    @Resource
    private CoachService coachService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CoachDto>> all() {
        return ResponseEntity.ok(coachService.getAllCoach());
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachDto> create(@AuthenticationPrincipal User user, @Valid @RequestBody CoachCreationDto dto) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(coachService.createCoach(user, dto));
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachDto> update(@AuthenticationPrincipal User user, @Valid @RequestBody CoachUpdateDto dto) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(coachService.updateCoach(user, dto));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('COACH', 'ADMIN')")
    public ResponseEntity<CoachDto> delete(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        coachService.deleteCoach(user);
        return ResponseEntity.ok().build();
    }
}
