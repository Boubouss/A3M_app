package com.A3M.user.dto.chaperone.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChaperoneUpdateDto {
    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String firstName;

    @Size(min = 4, max = 20, message = "Minimum 4 and maximum 20.")
    private String lastName;
}
