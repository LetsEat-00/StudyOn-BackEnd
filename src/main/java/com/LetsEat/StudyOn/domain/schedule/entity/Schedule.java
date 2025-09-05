package com.LetsEat.StudyOn.domain.schedule.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.domain.schedule.dto.ScheduleRequestDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "schedules",
        indexes = {
                @Index(name = "idx_user_division", columnList = "user_id, division_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 작성자
    private User user;
    @Column(name = "division_id", nullable = false)
    private Long divisionId;    // 일정 구분 :: 0 [일회성 공모], 그룹 ID [그룹 공모]
    private String title;
    private String content;
    @Column(name = "schedule_status", nullable = false)
    private String scheduleStatus;  // 일정 상태 : recruiting, closed, expired, hidden
    private String category;
    private String tags;
    @Column(name = "study_method")
    private String studyMethod;     // 스터디 방식 : online, offline
    private String location;
    @Column(name = "max_members")
    private Integer maxMembers;
    @Column(name = "start_dt")
    private LocalDateTime startDt;
    @Column(name = "end_dt")
    private LocalDateTime endDt;

    private Schedule(ScheduleRequestDto schedule) {
        this.user = schedule.getUser();
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
    }

    public static Schedule of(ScheduleRequestDto schedule) {
        return new Schedule(schedule);
    }
}
