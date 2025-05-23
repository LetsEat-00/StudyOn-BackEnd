package com.LetsEat.StudyOn.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{10,}$",
            message = "비밀번호는 최소 10자 이상이어야 하며, 영문 대소문자, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Size(min = 2, max = 10, message = "이름은 최소 2자 이상, 최대 10자 이하이어야 합니다.")
    private String name;

    @Size(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 최대 10자 이하이어야 합니다.")
    private String nickname;

}
