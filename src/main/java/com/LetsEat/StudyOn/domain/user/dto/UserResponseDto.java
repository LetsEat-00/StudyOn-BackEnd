package com.LetsEat.StudyOn.domain.user.dto;

import com.LetsEat.StudyOn.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String nickname;
    private final String profileImage;
    private final String statusMessage;

    private UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
        this.statusMessage = user.getStatusMessage();
    }

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user);
    }

}