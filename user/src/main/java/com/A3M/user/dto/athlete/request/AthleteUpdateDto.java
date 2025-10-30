package com.A3M.user.dto.athlete.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AthleteUpdateDto {
    @Size(min = 4, max = 20, message = "Minimum 8 and maximum 20.")
    private String firstName;

    @Size(min = 4, max = 20, message = "Minimum 8 and maximum 20.")
    private String lastName;

    @Past
    private LocalDate birthDate;

    private Boolean gender;
}
