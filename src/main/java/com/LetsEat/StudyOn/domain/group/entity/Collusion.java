package com.LetsEat.StudyOn.domain.group.entity;

import com.LetsEat.StudyOn.common.entity.Timestamped;
import com.LetsEat.StudyOn.common.enums.CollusionStatus;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.domain.group.dto.CollusionRequestDto;
import com.LetsEat.StudyOn.domain.group.dto.CollusionUpdateRequestDto;
import com.LetsEat.StudyOn.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collusion extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collusion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title; // 제목

    @Column(columnDefinition = "text")
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    private CollusionStatus collusionStatus; // 공고 상태

    private String category; // 카테고리

    private String tags; // 태그

    private int maxMembers; // 모집 최대 인원

    private int currentMembers; // 현재 모집 인원

    private LocalDateTime startAt; // 시작일시

    private LocalDateTime endAt; // 종료일시

    @OneToMany(mappedBy = "collusion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Applicant> applicantList = new ArrayList<>(); // 그룹 공고 지원 목록

    private Collusion(CollusionRequestDto requestDto, Group group, User user) {
        this.group = group;
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.collusionStatus = CollusionStatus.RECRUITING;
        this.category = requestDto.getCategory();
        this.tags = requestDto.getTags();
        this.maxMembers = requestDto.getMaxMembers();
        this.currentMembers = 0;
        this.startAt = requestDto.getStartAt();
        this.endAt = requestDto.getEndAt();
    }

    public static Collusion of(CollusionRequestDto requestDto, Group group, User user) {
        return new Collusion(requestDto, group, user);
    }

    // 그룹 공고 수정
    public void updateCollusion(CollusionUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.collusionStatus = CollusionStatus.valueOf(requestDto.getCollusionStatus().toUpperCase());
        this.category = requestDto.getCategory();
        this.tags = requestDto.getTags();
        this.maxMembers = requestDto.getMaxMembers();
        this.startAt = requestDto.getStartAt();
        this.endAt = requestDto.getEndAt();
    }

    // 그룹 공고 상태 확인 (지원 불가 시 예외처리)
    public void collusionStatusCheck() {
        if (collusionStatus == CollusionStatus.CLOSED) { // 모집 종료
            throw new CustomException(ErrorType.CLOSED_COLLUSION);
        }else if (collusionStatus == CollusionStatus.EXPIRED) { // 기한 종료
            throw new CustomException(ErrorType.EXPIRED_COLLUSION);
        }else if (collusionStatus == CollusionStatus.HIDDEN) { // 조회 불가
            throw new CustomException(ErrorType.INCOMPATIBLE_COLLUSION);
        }
    }
}
