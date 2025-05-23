package com.LetsEat.StudyOn.domain.user.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.domain.user.dto.SignupRequestDto;
import com.LetsEat.StudyOn.domain.user.dto.TokenRequestDto;
import com.LetsEat.StudyOn.domain.user.dto.TokenResponseDto;
import com.LetsEat.StudyOn.domain.user.dto.UserResponseDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
