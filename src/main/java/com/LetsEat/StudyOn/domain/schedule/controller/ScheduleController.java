package com.LetsEat.StudyOn.domain.schedule.controller;

import com.LetsEat.StudyOn.common.dto.CommonResponse;
import com.LetsEat.StudyOn.common.enums.ApplicantStatus;
import com.LetsEat.StudyOn.common.enums.CollusionStatus;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.domain.schedule.dto.ScheduleRequestDto;
import com.LetsEat.StudyOn.domain.schedule.dto.ScheduleResponseDto;
import com.LetsEat.StudyOn.domain.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.LetsEat.StudyOn.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 일회성 공모 생성
     * @return 결과 메시지
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestBody ScheduleRequestDto request) {
        request.setUser(userDetails.getUser());
        scheduleService.createSchedule(request);
        return getResponseEntity("일회성 공모 생성 성공");
    }

    /**
     * 일회성 공모 수정
     * @return 결과 메시지
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse<?>> updateSchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @PathVariable Long scheduleId,
                                                            @RequestBody ScheduleRequestDto request) {
        request.setScheduleId(scheduleId);
        request.setUser(userDetails.getUser());
        scheduleService.updateSchedule(request);
        return getResponseEntity("일회성 공모 수정 성공");
    }

    /**
     * 일회성 공모 목록 조회
     * @return 결과 메시지
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getSchedule(
            Pageable pageable,
            @RequestParam(defaultValue = "ALL") CollusionStatus collusionStatus) {
        return getResponseEntity(scheduleService.getScheduleList(pageable, collusionStatus).stream()
                .map(ScheduleResponseDto::of)
                .toList(),"일회성 공모 목록 조회 성공");
    }

    /**
     * 일회성 공모 상세 조회
     * @param scheduleId 공모 ID
     * @return 결과 DTO 및 결과 메시지
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse<?>> getScheduleDetail(@PathVariable Long scheduleId) {
        return getResponseEntity(ScheduleResponseDto.of(scheduleService.getScheduleDetail(scheduleId)),"일회성 공모 상세 조회 성공");
    }

    /**
     * 공모 지원
     * @param scheduleId 지원 할 공모ID
     * @return 결과 메시지
     */
    @PostMapping("/applications/{scheduleId}")
    public ResponseEntity<CommonResponse<?>> applySchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @PathVariable Long scheduleId,
                                                           @RequestBody ScheduleRequestDto request) {
        request.setScheduleId(scheduleId);
        request.setUser(userDetails.getUser());
        scheduleService.applySchedule(request);
        return getResponseEntity("일회성 공모 지원 성공");
    }

    /**
     * 공모 지원 수정
     * @param scheduleUserId 지원 수정 할 공모ID
     * @return 결과 메시지
     */
    @PutMapping("/applications/{scheduleUserId}")
    public ResponseEntity<CommonResponse<?>> updateApplySchedule(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                 @PathVariable Long scheduleUserId,
                                                                 @RequestBody ScheduleRequestDto request) {
        request.setScheduleUserId(scheduleUserId);
        request.setUser(userDetails.getUser());
        scheduleService.updateApplySchedule(request);
        return getResponseEntity("일회성 공모 지원 수정 성공");
    }

    /**
     * 일회성 공모 지원 목록 조회
     * @param scheduleId 공모 ID
     * @return 결과 DTO 및 결과 메시지
     */
    @GetMapping("/applications/{scheduleId}")
    public ResponseEntity<CommonResponse<?>> getScheduleApplyList(
            @PathVariable Long scheduleId,
            Pageable pageable) {
        return getResponseEntity(scheduleService.getScheduleApplyList(pageable, scheduleId).stream()
                .map(ScheduleResponseDto::of)
                .toList(),"일회성 공모 지원 목록 조회 성공");
    }

    /**
     * 일회성 공모 지원 승인/거절
     * @param scheduleUserId 공모 ID
     * @return 결과 DTO 및 결과 메시지
     */
    @PatchMapping("/applications/{scheduleUserId}")
    public ResponseEntity<CommonResponse<?>> patchScheduleStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "PENDING") ApplicantStatus applicantStatus,
            @PathVariable Long scheduleUserId) {
        scheduleService.patchScheduleStatus(userDetails,scheduleUserId,applicantStatus);
        return getResponseEntity("일회성 공모 지원 승인/거절 성공");
    }
}