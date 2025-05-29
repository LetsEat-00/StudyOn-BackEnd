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
}
