package com.A3M.user.dto.coach.request;

import com.A3M.user.model.Coach;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CoachCreationDto {
    @NotBlank
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String lastName;

    public Coach generate() {
        Coach coach = new Coach();
        coach.setFirstName(firstName);
        coach.setLastName(lastName);
        return coach;
    }
}
