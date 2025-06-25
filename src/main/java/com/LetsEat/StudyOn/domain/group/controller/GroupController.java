package com.LetsEat.StudyOn.domain.group.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.domain.group.dto.GroupRequestDto;
import com.LetsEat.StudyOn.domain.group.dto.GroupResponseDto;
import com.LetsEat.StudyOn.domain.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import static com.LetsEat.StudyOn.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * 그룹 생성
     *
     * @param requestDto  그룹 이름, 그룹 소개
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

    /**
     * 전체 그룹 목록 조회
     *
     * @return 모든 그룹 정보를 담은 리스트 반환
     * 페이징 추가하기
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getGroupList(Pageable pageable) {
        Slice<GroupResponseDto> groupList = groupService.getAllGroups(pageable);
        return getResponseEntity(groupList, "그룹 목록 조회 성공");
    }

    /**
     * 그룹 상세 조회
     *
     * @param groupId 조회할 그룹 ID
     * @return 해당 그룹의 상세 정보
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> getGroupById(@PathVariable Long groupId) {
        GroupResponseDto responseDto = groupService.getGroupById(groupId);
        return getResponseEntity(responseDto, "그룹 상세 조회 성공");
    }

    /**
     * 그룹 수정
     *
     * @param groupId     수정할 그룹 ID
     * @param requestDto  수정할 그룹 정보
     * @param userDetails 요청 사용자 정보
     * @return 수정된 그룹 정보
     */
    @PutMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> updateGroup(
            @PathVariable Long groupId,
            @Valid @RequestBody GroupRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        GroupResponseDto responseDto = groupService.updateGroup(groupId, requestDto, userDetails.getUser());
        return getResponseEntity(responseDto, "그룹 수정 성공");
    }

    /**
     * 그룹 탈퇴
     *
     * @param groupId     탈퇴할 그룹 ID
     * @param userDetails 요청 사용자
     * @return 성공 메시지
     */
    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<CommonResponse<?>> leaveGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        groupService.leaveGroup(groupId, userDetails.getUser());
        return getResponseEntity("그룹 탈퇴 성공");
    }

    /**
     * 그룹 삭제
     *
     * @param groupId     그룹 ID
     * @param userDetails 로그인 사용자 정보
     * @return 성공 메시지
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> deleteGroup(
            @PathVariable Long groupId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        groupService.deleteGroup(groupId, userDetails.getUser());
        return getResponseEntity("그룹 삭제 성공");
    }

}
