package com.LetsEat.StudyOn.domain.group.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.enums.ApplicantStatus;
import com.LetsEat.StudyOn.common.enums.CollusionStatus;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.domain.group.dto.*;
import com.LetsEat.StudyOn.domain.group.service.CollusionService;
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
@RequestMapping("/api/v1/groups/collusion")
public class CollusionController {

    private final CollusionService collusionService;

    /**
     * 그룹 공고 생성
     * @param requestDto 그룹 공고 정보
     * @param userDetails 사용자
     * @return 그룹 공고 정보
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createCollusion(
            @Valid @RequestBody CollusionRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CollusionResponseDto responseDto = collusionService.createCollusion(requestDto, userDetails.getUser());
        return getResponseEntity(responseDto, "그룹 공고 생성 성공");
    }

    /**
     * 그룹 공고 수정
     * @param requestDto 그룹 공고 정보
     * @param collusionId 그룹 공고 ID
     * @param userDetails 사용자
     * @return 그룹 공고 정보
     */
    @PutMapping("/{collusionId}")
    public ResponseEntity<CommonResponse<?>> updateCollusion(
            @Valid @RequestBody CollusionUpdateRequestDto requestDto,
            @PathVariable Long collusionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CollusionResponseDto responseDto = collusionService.updateCollusion(collusionId, requestDto, userDetails.getUser());
        return getResponseEntity(responseDto, "그룹 공고 수정 성공");
    }

    /**
     * 그룹 공고 상세조회
     * @param collusionId 그룹 공고 ID
     * @return 그룹 공고 정보
     */
    @GetMapping("/{collusionId}")
    public ResponseEntity<CommonResponse<?>> getCollusion(
            @PathVariable Long collusionId) {
        CollusionResponseDto responseDto = collusionService.getCollusion(collusionId);
        return getResponseEntity(responseDto, "그룹 공고 상세조회 성공");
    }

    /**
     * 상태별 그룹 공고 목록 조회
     * @param pageable 페이징
     * @param collusionStatus 그룹 공고 상태
     * @return 그룹 공고 목록
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getCollusionList(
            Pageable pageable,
            @RequestParam(defaultValue = "ALL") CollusionStatus collusionStatus) {
        Slice<CollusionResponseDto> collusionList = collusionService.getCollusionList(pageable, collusionStatus);
        return getResponseEntity(collusionList,"그룹 공고 목록 조회 성공");
    }

    /**
     * 그룹 공고 지원
     * @param collusionId 그룹 공고 ID
     * @param applicantRequestDto 지원사유
     * @param userDetails 사용자
     * @return 응답메시지
     */
    @PostMapping("/{collusionId}/applicants")
    public ResponseEntity<CommonResponse<?>> createApplicant(
            @PathVariable Long collusionId,
            @RequestBody ApplicantRequestDto applicantRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        collusionService.createApplicant(collusionId, applicantRequestDto, userDetails.getUser());
        return getResponseEntity("그룹 공고 지원 성공");
    }

    /**
     * 그룹 공고 지원자 목록 조회
     * @param pageable 페이징
     * @param collusionId 그룹 공고 ID
     * @param applicantStatus 지원상태
     * @param userDetails 사용자
     * @return 그룹 공고 지원자 목록
     */
    @GetMapping("/{collusionId}/applicants")
    public ResponseEntity<CommonResponse<?>> getApplicantList(
            Pageable pageable,
            @PathVariable Long collusionId,
            @RequestParam(defaultValue = "PENDING") ApplicantStatus applicantStatus,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Slice<ApplicantResponseDto> applicantList =
                collusionService.getApplicantList(collusionId, applicantStatus, pageable, userDetails.getUser());
        return getResponseEntity(applicantList, "그룹 공고 지원자 목록 조회 성공");
    }

    /**
     * 그룹 공고 지원 승인/거절
     * @param collusionId 그룹 공고 ID
     * @param userId 그룹 공고 지원자
     * @param applicantStatus 지원자 상태
     * @param userDetails 사용자
     * @return 그룹 공고 승인/거절 응답메시지
     */
    @PatchMapping("/{collusionId}/applicants/{userId}")
    public ResponseEntity<CommonResponse<?>> updateApplicants(
            @PathVariable Long collusionId,
            @PathVariable Long userId,
            @RequestParam(defaultValue = "PENDING") ApplicantStatus applicantStatus,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String message = collusionService.updateApplicants(collusionId, userId, applicantStatus, userDetails.getUser());
        return getResponseEntity(message);
    }
}
