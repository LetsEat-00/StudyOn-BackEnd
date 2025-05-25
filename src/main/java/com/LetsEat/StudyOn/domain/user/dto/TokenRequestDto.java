package com.LetsEat.StudyOn.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {

    @NotBlank(message = "accessToken 이 존재하지 않습니다.")
    private String accessToken;

    @NotBlank(message = "refreshToken 이 존재하지 않습니다.")
    private String refreshToken;
}
