package com.LetsEat.StudyOn.domain.schedule.dto;

import com.LetsEat.StudyOn.domain.schedule.entity.Schedule;
import com.LetsEat.StudyOn.domain.schedule.entity.ScheduleUser;
import com.LetsEat.StudyOn.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private Long userId;
    private Long scheduleId;
    private Long divisionId;
    private String title;
    private String content;
    private String category;
    private String tags;
    private String studyMethod;
    private String location;
    private Integer maxMembers;
    private String scheduleStatus;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 지원할 때 필요한 추가 정보
    private Long scheduleUserId;
    private Schedule schedule;
    private User user;
    private String reason;
    private String status;

    // 일회성 모집 목록 조회
    private ScheduleResponseDto(Schedule schedule) {
        this.userId = schedule.getUser().getId();
        this.scheduleId = schedule.getScheduleId();
        this.divisionId = schedule.getDivisionId();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.category = schedule.getCategory();
        this.tags = schedule.getTags();
        this.studyMethod = schedule.getStudyMethod();
        this.location = schedule.getLocation();
        this.maxMembers = schedule.getMaxMembers();
        this.scheduleStatus = schedule.getScheduleStatus();
        this.startDt = schedule.getStartDt();
        this.endDt = schedule.getEndDt();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }

    public static ScheduleResponseDto of(Schedule schedule) {
        return new ScheduleResponseDto(schedule);
    }

    // 일회성 모집 지원 목록 조회
    private ScheduleResponseDto(ScheduleUser scheduleUser) {
        this.scheduleUserId = scheduleUser.getScheduleUserId();
        this.schedule = scheduleUser.getSchedule();
        this.user = scheduleUser.getUser();
        this.reason = scheduleUser.getReason();
        this.status = scheduleUser.getStatus();
    }

    public static ScheduleResponseDto of(ScheduleUser scheduleUser) {
        return new ScheduleResponseDto(scheduleUser);
    }
}
