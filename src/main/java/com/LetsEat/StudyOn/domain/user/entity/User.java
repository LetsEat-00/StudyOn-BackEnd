package com.LetsEat.StudyOn.domain.user.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.common.enums.UserRole;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.domain.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    private String password;

    private String name;

    private String nickname;

    private String profileImage;

    private String statusMessage;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followerList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Setter
    private String refreshToken;

    private User(SignupRequestDto dto, String encodedPassword) {
        email = dto.getEmail();
        password = encodedPassword;
        name = dto.getName();
        nickname = dto.getNickname();
        userRole = UserRole.USER;
    }

    public static User of(SignupRequestDto dto, String encodedPassword) {
        return new User(dto, encodedPassword);
    }

    public void checkUserRole() {
        if (!UserRole.ADMIN.equals(this.userRole)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean checkName(String name) {
        return this.name.equals(name);
    }
}
