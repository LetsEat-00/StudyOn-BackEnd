package com.LetsEat.StudyOn.domain.group.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.common.enums.MemberRole;
import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Timestamped {

    @EmbeddedId
    private MemberId id;

    @MapsId("user")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 시용자

    @MapsId("group")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group; // 그룹

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole; // 멤버 역할

    private Member(User user, Group group, MemberRole memberRole) {
        this.id = new MemberId(user, group);
        this.memberRole = memberRole;
    }

    public static Member of(User user, Group group, MemberRole memberRole) {
        return new Member(user, group, memberRole);
    }
}
