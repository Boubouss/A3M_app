package com.A3M.user.dto.athlete.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AthleteSearchAgeDto {
    @NotNull
    private Integer minAge;

    @NotNull
    private Integer maxAge;
}
