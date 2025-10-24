package com.A3M.user.dto.user;

import com.A3M.user.model.Judoka;
import com.A3M.user.model.User;
import lombok.Data;

import java.util.Set;

@Data
public class UserWithJudokaDto {
    private Long id;
    private String email;
    private String phoneNumber;
    private Set<Judoka> judokas;

    public static UserWithJudokaDto from(User user) {
        UserWithJudokaDto userDto = new UserWithJudokaDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setJudokas(user.getJudokas());
        return userDto;
    }
}