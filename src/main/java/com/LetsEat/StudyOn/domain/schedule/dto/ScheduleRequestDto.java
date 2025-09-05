package com.LetsEat.StudyOn.domain.schedule.dto;

import com.LetsEat.StudyOn.domain.schedule.entity.Schedule;
import com.LetsEat.StudyOn.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequestDto {

    private User user;
    private Long divisionId;
    private String title;
    private String content;
    private String category;
    private String tags;
    private String studyMethod;
    private String location;
    private Integer maxMembers;
    private LocalDateTime startDt;
    private LocalDateTime endDt;

    // 지원할 때 필요한 추가 정보
    private Schedule schedule;
    private Long scheduleId;
    private String reason;
    private String applyStatus;

    // 수정할 때 필요한 추가 정보
    private Long scheduleUserId;
    private String scheduleStatus;
}