package com.A3M.user.dto.judoka;

import com.A3M.user.dto.user.UserLightDto;
import com.A3M.user.model.Judoka;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JudokaDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Boolean gender;
    private Long licenseId;
    private String beltLevel;
    private UserLightDto user;

    public static JudokaDto from(Judoka judoka){
        JudokaDto judokaDto = new JudokaDto();
        judokaDto.setId(judoka.getId());
        judokaDto.setFirstName(judoka.getFirstName());
        judokaDto.setLastName(judoka.getLastName());
        judokaDto.setBirthDate(judoka.getBirthDate());
        judokaDto.setGender(judoka.getGender());
        judokaDto.setLicenseId(judoka.getLicenseId());
        judokaDto.setBeltLevel(String.valueOf(judoka.getBeltLevel()));
        judokaDto.setUser(UserLightDto.from(judoka.getUser()));
        return judokaDto;
    }
}
