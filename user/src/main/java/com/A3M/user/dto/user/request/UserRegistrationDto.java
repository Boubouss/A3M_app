package com.A3M.user.dto.user.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 20, message = "Minimum 8 and maximum 20.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character."
    )
    private String password;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must be a 10-digit number (e.g., 0612345678)"
    )
    private String phoneNumber;

}