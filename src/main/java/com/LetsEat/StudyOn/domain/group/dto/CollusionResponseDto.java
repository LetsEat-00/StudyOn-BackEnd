package com.LetsEat.StudyOn.domain.group.dto;

import com.LetsEat.StudyOn.domain.group.entity.Collusion;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CollusionResponseDto {

    private final Long collusionId;
    private final Long groupId;
    private final Long userId;
    private final String title;
    private final String content;
    private final String collusionStatus;
    private final String category;
    private final String tags;
    private final int maxMembers;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public CollusionResponseDto(Collusion collusion) {
        this.collusionId = collusion.getId();
        this.groupId = collusion.getGroup().getId();
        this.userId = collusion.getUser().getId();
        this.title = collusion.getTitle();
        this.content = collusion.getContent();
        this.collusionStatus = collusion.getCollusionStatus().toString();
        this.category = collusion.getCategory();
        this.tags = collusion.getTags();
        this.maxMembers = collusion.getMaxMembers();
        this.startAt = collusion.getStartAt();
        this.endAt = collusion.getEndAt();
    }


    public static CollusionResponseDto of(Collusion collusion) {
        return new CollusionResponseDto(collusion);
    }
}
