package com.A3M.user.controller;

import com.A3M.user.dto.athlete.request.AthleteSearchNameDto;
import com.A3M.user.dto.athlete.response.AthleteDto;
import com.A3M.user.dto.chaperone.request.ChaperoneCreationDto;
import com.A3M.user.dto.chaperone.request.ChaperoneSearchDto;
import com.A3M.user.dto.chaperone.request.ChaperoneUpdateDto;
import com.A3M.user.dto.chaperone.response.ChaperoneDto;
import com.A3M.user.exception.type.UserNotFoundException;
import com.A3M.user.model.Chaperone;
import com.A3M.user.model.User;
import com.A3M.user.repository.ChaperoneRepository;
import com.A3M.user.services.ChaperoneService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chaperone")
public class ChaperoneController {
    @Resource
    private ChaperoneService chaperoneService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'STAFF')")
    public ResponseEntity<List<ChaperoneDto>> all() {
        return ResponseEntity.ok(chaperoneService.getAllChaperones());
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH', 'STAFF')")
    public ResponseEntity<List<ChaperoneDto>> searchByName(@Valid @RequestBody ChaperoneSearchDto dto) {
        if (dto == null) {
            return ResponseEntity.ok(chaperoneService.getAllChaperones());
        }
        return ResponseEntity.ok(chaperoneService.getChaperoneByName(dto));
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChaperoneDto> create(@AuthenticationPrincipal User user, @Valid @RequestBody ChaperoneCreationDto dto) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(chaperoneService.createChaperone(user, dto));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChaperoneDto> update(@AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody ChaperoneUpdateDto dto) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        return ResponseEntity.ok(chaperoneService.updateChaperone(user, id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChaperoneDto> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        if (user == null) {
            throw new UserNotFoundException("User not found in security context");
        }
        chaperoneService.deleteChaperone(user, id);
        return ResponseEntity.ok().build();
    }
}
