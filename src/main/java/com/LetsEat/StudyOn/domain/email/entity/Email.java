package com.LetsEat.StudyOn.domain.email.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "email")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "email_status", nullable = false)
    private boolean emailStatus;

    private Email(String email, String code) {
        this.email = email;
        this.code = code;
        this.emailStatus = false;
    }

    public static Email of(String email, String code) {
        return new Email(email, code);
    }

    public void updateCode(String code) {
        this.code = code;
    }

    public void emailAuth(String code) {
        if (!code.equals(this.code)) {
            throw new CustomException(ErrorType.INVALID_AUTH_CODE);
        }
        this.emailStatus = true;
    }
}
