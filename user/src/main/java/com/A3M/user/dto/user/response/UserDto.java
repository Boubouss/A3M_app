package com.A3M.user.dto.user.response;

import com.A3M.user.model.Chaperone;
import com.A3M.user.model.Coach;
import com.A3M.user.model.Judoka;
import com.A3M.user.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Set<Judoka> judokas;
    private Set<Chaperone> chaperones;
    private Coach coach;

    public static UserDto from(@NotNull User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setJudokas(user.getJudokas());
        userDto.setChaperones(user.getChaperones());
        userDto.setCoach(user.getCoach());
        return userDto;
    }
}