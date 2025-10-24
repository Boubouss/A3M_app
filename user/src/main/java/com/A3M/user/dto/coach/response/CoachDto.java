package com.A3M.user.dto.coach.response;

import com.A3M.user.dto.user.response.UserLightDto;
import com.A3M.user.model.Coach;
import lombok.Data;

@Data
public class CoachDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long licenseId;
    private UserLightDto user;

    public static CoachDto from(Coach coach) {
        CoachDto coachDto = new CoachDto();
        coachDto.setId(coach.getId());
        coachDto.setFirstName(coach.getFirstName());
        coachDto.setLastName(coach.getLastName());
        coachDto.setLicenseId(coach.getLicenseId());
        coachDto.setUser(UserLightDto.from(coach.getUser()));
        return coachDto;
    }
}