package com.A3M.user.dto.athlete.response;

import com.A3M.user.dto.user.response.UserLightDto;
import com.A3M.user.model.Athlete;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AthleteDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Boolean gender;
    private Long licenseId;
    private Long athleteDetailsId;
    private UserLightDto user;

    public static AthleteDto from(Athlete athlete){
        AthleteDto athleteDto = new AthleteDto();
        athleteDto.setId(athlete.getId());
        athleteDto.setFirstName(athlete.getFirstName());
        athleteDto.setLastName(athlete.getLastName());
        athleteDto.setBirthDate(athlete.getBirthDate());
        athleteDto.setGender(athlete.getGender());
        athleteDto.setLicenseId(athlete.getLicenseId());
        athleteDto.setAthleteDetailsId(athlete.getAthleteDetailsId());
        athleteDto.setUser(UserLightDto.from(athlete.getUser()));
        return athleteDto;
    }
}
