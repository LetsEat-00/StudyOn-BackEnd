package com.LetsEat.StudyOn.domain.group.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.domain.group.dto.GroupRequestDto;
import com.LetsEat.StudyOn.domain.group.dto.GroupResponseDto;
import com.LetsEat.StudyOn.domain.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.LetsEat.StudyOn.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * 그룹 생성
     * @param requestDto 그룹 이름, 그룹 소개
     * @param userDetails 사용자
     * @return 그룹 정보, 그룹 생성자 ID
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createGroup(
            @Valid @RequestBody GroupRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        GroupResponseDto responseDto = groupService.createGroup(requestDto, userDetails.getUser());
        return getResponseEntity(responseDto, "그룹 생성 성공");
    }
}
