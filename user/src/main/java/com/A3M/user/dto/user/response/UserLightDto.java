package com.A3M.user.dto.user.response;

import com.A3M.user.model.User;
import lombok.Data;

@Data
public class UserLightDto {
    private Long id;
    private String email;
    private String phoneNumber;

    public static UserLightDto from(User user) {
        UserLightDto userDto = new UserLightDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }
}
