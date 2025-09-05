package com.LetsEat.StudyOn.domain.group.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.common.enums.ApplicantStatus;
import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicant extends Timestamped {

    @EmbeddedId
    private ApplicantId id;

    @MapsId("user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("collusion")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collusion_id", nullable = false)
    private Collusion collusion;

    @Enumerated(EnumType.STRING)
    private ApplicantStatus applicantStatus; // 지원상태

    private String reason; // 지원사유

    private Applicant(String reason, Collusion collusion, User user) {
        this.id = new ApplicantId(user, collusion);
        this.applicantStatus = ApplicantStatus.PENDING;
        this.reason = reason;
    }

    public static Applicant of(String reason, Collusion collusion, User user) {
        return new Applicant(reason, collusion, user);
    }

    // 그룹 공고 지원 상태변경
    public void updateApplicantStatus(ApplicantStatus applicantStatus) {
        this.applicantStatus = applicantStatus;
    }
}
