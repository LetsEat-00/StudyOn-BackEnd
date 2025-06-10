package com.LetsEat.StudyOn.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static com.LetsEat.StudyOn.common.config.page.CustomPageableHandlerMethodArgumentResolver.MAX_PAGE_SIZE;

@Getter
@AllArgsConstructor
public enum ErrorType {

    // User
    DUPLICATED_EMAIL(BAD_REQUEST, "이미 존재하는 사용자 이메일입니다."),
    INVALID_EMAIL(UNAUTHORIZED, "잘못된 이메일입니다."),
    INVALID_PASSWORD(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_NAME(UNAUTHORIZED, "이름이 일치하지 않습니다."),
    DUPLICATE_PASSWORD(LOCKED, "중복된 비밀번호입니다."),
    FAILED_TO_BE_LOGIN(FORBIDDEN, "로그인이 실패하였습니다."),
    DEACTIVATE_USER(FORBIDDEN, "이미 탈퇴한 회원입니다."),
    NO_AUTHENTICATION(FORBIDDEN, "권한이 없습니다."),
    NOT_FOUND_USER(NOT_FOUND, "회원을 찾을 수 없습니다."),
    REQUIRES_LOGIN(UNAUTHORIZED, "로그인이 필요한 서비스입니다."),

    // Mail
    NOT_FOUND_MAIL(NOT_FOUND, "인증 메일 전송 내역을 찾을 수 없습니다."),
    MAIL_SEND_PROCESS_ERROR(UNAUTHORIZED, "메일 전송 과정 중 문제가 생겼습니다."),
    UNSUPPORTED_ENCODING(UNAUTHORIZED, "지원되지 않는 문자 인코딩입니다."),
    INVALID_AUTH_CODE(UNAUTHORIZED, "잘못된 인증코드입니다."),

    // JWT
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
    INVALID_USER_INFO(UNAUTHORIZED, "토큰과 사용자 정보가 일치하지 않습니다."),
    NOT_FOUND_AUTHENTICATION_INFO(NOT_FOUND, "인증 정보를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(NOT_FOUND, "토큰을 찾을 수 없습니다."),
    INVALID_JWT(UNAUTHORIZED, "유효하지 않는 JWT 입니다."),
    EXPIRED_JWT(FORBIDDEN, "만료된 JWT 입니다."),
    UNSUPPORTED_JWT(BAD_REQUEST, "지원되지 않는 JWT입니다."),
    LOGGED_OUT_TOKEN(FORBIDDEN, "이미 로그아웃된 토큰입니다."),

    // Follow
    CANT_FOLLOW_YOURSELF(UNAUTHORIZED,"자기 자신은 팔로우할 수 없습니다."),
    ALREADY_FOLLOWED(UNAUTHORIZED,"이미 팔로우한 사용자 입니다."),
    ALREADY_UNFOLLOWED(UNAUTHORIZED,"이미 언팔로우한 사용자 입니다."),


    // group
    NOT_FOUND_GROUP(NOT_FOUND, "그룹을 찾을 수 없습니다."),
    ALREADY_GROUP(UNAUTHORIZED,"이미 속한 그룹입니다."),

    // collusion
    NOT_FOUND_COLLUSION(NOT_FOUND, "그룹 공고를 찾을 수 없습니다."),
    EXPIRED_COLLUSION(FORBIDDEN, "기한이 종료된 그룹 공고입니다."),
    CLOSED_COLLUSION(FORBIDDEN, "모집이 종료된 그룹 공고입니다."),
    INCOMPATIBLE_COLLUSION(FORBIDDEN, "지원할 수 없는 그룹 공고입니다."),
    ALREADY_APPLIED_COLLUSION(UNAUTHORIZED,"이미 지원한 그룹 공고입니다."),

    // Page
    INVALID_PAGE_NUMBER_FORMAT(BAD_REQUEST, "숫자 형식이 아닌 페이지 number입니다."),
    INVALID_PAGE_SIZE_FORMAT(BAD_REQUEST, "숫자 형식이 아닌 페이지 size입니다."),
    INVALID_PAGE_NUMBER(BAD_REQUEST, "페이지 number는 음수일 수 없습니다."),
    INVALID_PAGE_SIZE(BAD_REQUEST, "페이지 size는 0 이하일 수 없습니다."),
    EXCEED_MAX_PAGE_SIZE(BAD_REQUEST, "페이지 size는 " + MAX_PAGE_SIZE + "을 초과할 수 없습니다."),
    EMPTY_PAGE_ELEMENTS(BAD_REQUEST, "페이지의 요소가 존재하지 않습니다."),
    PAGE_NOT_FOUND(BAD_REQUEST, "존재하지 않는 페이지입니다."),

    // Applicant
    NOT_FOUND_APPLICANT(NOT_FOUND, "그룹 공고 지원 현황을 찾을 수 없습니다."),
    INVALID_APPLICANT_STATUS(BAD_REQUEST, "잘못된 그룹 공고 지원자 상태입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;

}