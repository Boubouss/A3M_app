package com.A3M.user.dto.user.response;

import com.A3M.user.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoggedDto {
    private UserDto user;
    private String token;
    private String type = "Bearer";
}
