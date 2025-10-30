package com.A3M.user.dto.chaperone.request;

import com.A3M.user.model.Chaperone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChaperoneCreationDto {
    @NotBlank
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String firstName;

    @NotBlank
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String lastName;

    public Chaperone generate() {
        Chaperone chaperone = new Chaperone();
        chaperone.setFirstName(firstName);
        chaperone.setLastName(lastName);
        return chaperone;
    }
}
