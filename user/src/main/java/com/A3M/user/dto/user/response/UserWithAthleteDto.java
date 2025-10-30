package com.A3M.user.dto.user.response;

import com.A3M.user.model.Athlete;
import com.A3M.user.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserWithAthleteDto {
    private Long id;
    private String email;
    private String phoneNumber;
    private Set<Athlete> athletes;

    public static UserWithAthleteDto from(User user) {
        UserWithAthleteDto userDto = new UserWithAthleteDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setAthletes(user.getAthletes());
        return userDto;
    }
}