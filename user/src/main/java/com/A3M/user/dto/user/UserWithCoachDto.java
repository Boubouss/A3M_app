package com.A3M.user.dto.user;

import com.A3M.user.model.Coach;
import com.A3M.user.model.User;
import lombok.Data;

@Data
public class UserWithCoachDto {
    private Long id;
    private String email;
    private String phoneNumber;
    private Coach coach;

    public static UserWithCoachDto from(User user) {
        UserWithCoachDto userDto = new UserWithCoachDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setCoach(user.getCoach());
        return userDto;
    }
}