package com.LetsEat.StudyOn.domain.group.entity;

import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ApplicantId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collusion_id", nullable = false)
    private Collusion collusion; // 그룹 공고
}
