package com.A3M.user.dto.user.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserLoginDto {

    @NotBlank(message = "Username is required.")
    @Email(message = "Username must be valid.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 20, message = "Minimum 8 and maximum 20.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character."
    )
    private String password;

}
