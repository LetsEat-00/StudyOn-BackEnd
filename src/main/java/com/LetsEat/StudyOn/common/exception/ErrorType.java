package com.LetsEat.StudyOn.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // User
    DUPLICATED_EMAIL(BAD_REQUEST, "이미 존재하는 사용자 이메일입니다."),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "잘못된 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_NAME(HttpStatus.UNAUTHORIZED, "이름이 일치하지 않습니다."),
    DUPLICATE_PASSWORD(HttpStatus.LOCKED, "중복된 비밀번호입니다."),
    FAILED_TO_BE_LOGIN(HttpStatus.FORBIDDEN, "로그인이 실패하였습니다."),
    DEACTIVATE_USER(HttpStatus.FORBIDDEN, "이미 탈퇴한 회원입니다."),
    NO_AUTHENTICATION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    REQUIRES_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),

    // Mail
    NOT_FOUND_MAIL(HttpStatus.UNAUTHORIZED, "인증 메일 전송 내역을 찾을 수 없습니다."),
    MAIL_SEND_PROCESS_ERROR(HttpStatus.UNAUTHORIZED, "메일 전송 과정 중 문제가 생겼습니다."),
    UNSUPPORTED_ENCODING(HttpStatus.UNAUTHORIZED, "지원되지 않는 문자 인코딩입니다."),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "잘못된 인증코드입니다."),

    // JWT
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
    INVALID_USER_INFO(HttpStatus.UNAUTHORIZED, "토큰과 사용자 정보가 일치하지 않습니다."),
    NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.NOT_FOUND, "인증 정보를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 입니다."),
    EXPIRED_JWT(HttpStatus.FORBIDDEN, "만료된 JWT 입니다."),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT입니다."),
    LOGGED_OUT_TOKEN(HttpStatus.FORBIDDEN, "이미 로그아웃된 토큰입니다."),

    // Follow
    CANT_FOLLOW_YOURSELF(HttpStatus.UNAUTHORIZED,"자기 자신은 팔로우할 수 없습니다."),
    ALREADY_FOLLOWED(HttpStatus.UNAUTHORIZED,"이미 팔로우한 사용자 입니다."),
    ALREADY_UNFOLLOWED(HttpStatus.UNAUTHORIZED,"이미 언팔로우한 사용자 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}