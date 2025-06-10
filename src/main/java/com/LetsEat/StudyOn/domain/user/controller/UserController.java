package com.LetsEat.StudyOn.domain.user.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.domain.user.dto.*;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.LetsEat.StudyOn.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     * @param dto 회원가입 시 필요한 정보
     * @return 회원가입된 사용자 정보와 응답 메시지를 포함한 ResponseEntity
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequestDto dto) {

        User user = userService.signup(dto);
        return getResponseEntity(UserResponseDto.of(user), "회원가입 성공");
    }

    /**
     * Access/Refresh 토큰 재발급
     * @param tokenRequestDto Access/Refresh 토큰 정보
     * @return 재발급된 Access/Refresh 또는 Access 토큰 정보
     */
    @PostMapping("/auth/reissue")
    public ResponseEntity<CommonResponse<?>> reissue(
            @Valid @RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = userService.reissue(tokenRequestDto);
        return getResponseEntity(tokenResponseDto, "AccessToken & RefreshToken 재발급");
    }

    /**
     * 사용자 비밀번호 변경
     * @param passwordDto 현재, 새로운 비밀번호
     * @param userDetails 사용자 정보
     * @return 응답 메시지
     */
    @PatchMapping("/auth/password")
    public ResponseEntity<CommonResponse<?>> updatePassword(
            @Valid @RequestBody PasswordUpdateDto passwordDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePassword(passwordDto, userDetails.getUser());
        return getResponseEntity("비밀번호 변경 성공");
    }

    /**
     * 비밀번호 찾기 (새비밀번호 메일로 발송)
     * @param passwordFindDto 이메일, 이름
     * @return 응답 메시지
     */
    @PutMapping("/auth/password")
    public ResponseEntity<CommonResponse<?>> findPassword(
            @Valid @RequestBody PasswordFindDto passwordFindDto) {
        userService.findPassword(passwordFindDto);
        return getResponseEntity("새비밀번호 메일 발송 성공");
    }
    /**
     * 내 프로필 정보 조회
     * @param userDetails 인증된 사용자 정보
     * @return 내 프로필 정보 DTO와 응답 메시지를 포함한 ResponseEntity
     */
    @GetMapping("/users/me")
    public ResponseEntity<CommonResponse<?>> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return getResponseEntity(UserResponseDto.of(userDetails.getUser()), "내 프로필 조회 성공");
    }
    /**
     * 내 프로필 정보 수정
     * @param dto 수정할 사용자 정보 (이름, 닉네임, 프로필 이미지 등)
     * @param userDetails 인증된 사용자 정보
     * @return 응답 메시지
     */
    @PutMapping("/users/me")
    public ResponseEntity<CommonResponse<?>> updateMyProfile(
            @Valid @RequestBody UserUpdateRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updateMyProfile(dto, userDetails.getUser());
        return getResponseEntity("내 프로필 수정 성공");
    }
    /**
     * 특정 사용자 프로필 상세 조회
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자 프로필 정보와 응답 메시지를 포함한 ResponseEntity
     */
    @GetMapping("/users/profile/{userId}")
    public ResponseEntity<CommonResponse<?>> getUserProfile(@PathVariable Long userId) {
        return getResponseEntity(userService.getUserProfile(userId), "타인 프로필 조회 성공");
    }



}
