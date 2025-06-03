package com.LetsEat.StudyOn.domain.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String nickname;
    private String profileImage;
    private String statusMessage;
}
