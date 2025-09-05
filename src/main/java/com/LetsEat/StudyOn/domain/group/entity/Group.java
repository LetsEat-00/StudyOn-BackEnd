package com.LetsEat.StudyOn.domain.group.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.common.enums.MemberRole;
import com.LetsEat.StudyOn.domain.group.dto.GroupRequestDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String groupName; // 그룹 이름

    private String description; // 그룹 소개

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> memberList = new ArrayList<>(); // 멤버 목록

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collusion> collusionList = new ArrayList<>(); // 그룹 공고 목록

    private Group(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public static Group of(GroupRequestDto requestDto) {
        return new Group(requestDto.getGroupName(), requestDto.getDescription());
    }

    // 그룹의 owner 인지 확인
    public boolean isOwner(User user) {
        return memberList.stream()
                .anyMatch(member -> member.getId().getUser().getId().equals(user.getId())
                        && member.getMemberRole() == MemberRole.OWNER);
    }

    // 그룹에 속해 있는 사용자인지 확인
    public boolean isMember(User user) {
        return memberList.stream()
                .anyMatch(member -> member.getId().getUser().equals(user));
    }

    // 그룹 멤버 추가
    public void addMember(User user) {
        this.memberList.add(Member.of(user, this, MemberRole.MEMBER));
    }

    // 그룹 업데이트
    public void update(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }
}
