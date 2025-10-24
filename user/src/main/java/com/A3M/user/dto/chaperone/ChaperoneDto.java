package com.A3M.user.dto.chaperone;

import com.A3M.user.dto.user.UserLightDto;
import com.A3M.user.model.Chaperone;
import lombok.Data;

@Data
public class ChaperoneDto {
    private Long id;
    private String firstName;
    private String lastName;
    private UserLightDto user;

    public static ChaperoneDto from(Chaperone chaperone) {
        ChaperoneDto chaperoneDto = new ChaperoneDto();
        chaperoneDto.setId(chaperone.getId());
        chaperoneDto.setFirstName(chaperone.getFirstName());
        chaperoneDto.setLastName(chaperone.getLastName());
        chaperoneDto.setUser(UserLightDto.from(chaperone.getUser()));
        return chaperoneDto;
    }
}