package com.LetsEat.StudyOn.domain.user.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.domain.user.dto.UserResponseDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.LetsEat.StudyOn.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequestMapping("/api/v1/users/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 팔로우
     * @param followingId 팔로우 할 대상
     * @return 결과 메시지
     */
    @PostMapping("/{followingId}")
    public ResponseEntity<CommonResponse<?>> follow(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable Long followingId) {
        followService.follow(userDetails.getUser().getId(), followingId);
        return getResponseEntity("팔로우 성공");
    }
    /**
     * 언팔로우
     * @param followingId 언팔로우 할 대상
     * @return 결과 메시지
     */
    @DeleteMapping("/{followingId}")
    public ResponseEntity<CommonResponse<?>> unfollow(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long followingId) {
        followService.unfollow(userDetails.getUser().getId(), followingId);
        return getResponseEntity("언팔로우 성공");
    }

    /**
     * 팔로잉 목록 조회
     * @param userId 목록 조회 할 대상
     * @return 팔로잉 사용자들 목록 및 결과 메시지
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<CommonResponse<?>> getFollowingList(@PathVariable Long userId) {
        List<User> users = followService.getFollowingList(userId);
        List<UserResponseDto> result = users.stream()
                .map(UserResponseDto::of)
                .toList();
        return getResponseEntity(result,"팔로잉 목록 조회 성공");
    }

    /**
     * 팔로워 목록 조회
     * @param userId 목록 조회 할 대상
     * @return 팔로워 사용자들 목록 및 결과 메시지
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<CommonResponse<?>> getFollowerList(@PathVariable Long userId) {
        List<User> users = followService.getFollowerList(userId);
        List<UserResponseDto> result = users.stream()
                .map(UserResponseDto::of)
                .toList();
        return getResponseEntity(result,"팔로워 목록 조회 성공");
    }
}