package com.LetsEat.StudyOn.domain.email.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyDto {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "인증번호를 입력해 주세요.")
    private String code;

}
