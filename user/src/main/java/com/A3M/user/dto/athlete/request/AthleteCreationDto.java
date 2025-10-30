package com.A3M.user.dto.athlete.request;

import com.A3M.user.model.Athlete;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AthleteCreationDto {
    @NotBlank
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String lastName;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotNull
    private Boolean gender;

    public Athlete generate() {
        Athlete athlete = new Athlete();
        athlete.setFirstName(firstName);
        athlete.setLastName(lastName);
        athlete.setGender(gender);
        athlete.setBirthDate(birthDate);
        return athlete;
    }
}
