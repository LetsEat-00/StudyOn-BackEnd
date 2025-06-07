package com.LetsEat.StudyOn.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Size(min = 2, max = 10, message = "이름은 최소 2자 이상, 최대 10자 이하이어야 합니다.")
    private String name;

    @Size(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 최대 10자 이하이어야 합니다.")
    private String nickname;

    private String profileImage;

    @Size(max = 50, message = "상태 메시지는 최대 50자까지 입력할 수 있습니다.")
    private String statusMessage;
}
