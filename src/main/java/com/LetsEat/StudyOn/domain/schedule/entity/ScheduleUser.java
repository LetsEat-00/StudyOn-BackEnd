package com.LetsEat.StudyOn.domain.schedule.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.domain.schedule.dto.ScheduleRequestDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "schedule_user",
        indexes = {
                @Index(name = "idx_schedule_user", columnList = "schedule_id, user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleUser extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_user_id")
    private Long scheduleUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false) // 공모번호
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 작성자
    private User user;

    @Column(nullable = false)
    private String status; // pending, accepted, rejected

    @Column(columnDefinition = "TEXT")
    private String reason;

    private ScheduleUser(ScheduleRequestDto scheduleUser) {
        this.schedule = scheduleUser.getSchedule();
        this.user = scheduleUser.getUser();
        this.reason = scheduleUser.getReason();
        this.status = scheduleUser.getApplyStatus();
    }

    public static ScheduleUser of(ScheduleRequestDto scheduleUser) {
        return new ScheduleUser(scheduleUser);
    }
}