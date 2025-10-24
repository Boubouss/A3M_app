package com.A3M.user.dto.user.response;

import com.A3M.user.model.Chaperone;
import com.A3M.user.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserWithChaperoneDto {
    public Long id;
    public String email;
    public String phoneNumber;
    public Set<Chaperone> chaperones;

    public static UserWithChaperoneDto from(User user) {
        UserWithChaperoneDto userDto = new UserWithChaperoneDto();
        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.phoneNumber = user.getPhoneNumber();
        userDto.chaperones = user.getChaperones();
        return userDto;
    }
}
