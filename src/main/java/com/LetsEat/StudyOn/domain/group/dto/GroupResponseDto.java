package com.LetsEat.StudyOn.domain.group.dto;

import com.LetsEat.StudyOn.domain.group.entity.Group;
import lombok.Getter;

@Getter
public class GroupResponseDto {

    private final Long id;
    private final String groupName;
    private final String description;
    private final Long ownerId;

    private GroupResponseDto(Group group, Long id) {
        this.id = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.ownerId = id;
    }

    private GroupResponseDto(Long id, String groupName) {
        this.id = id;
        this.groupName = groupName;
        this.description = null;
        this.ownerId = null;
    }

    public static GroupResponseDto of(Group group, Long id) {
        return new GroupResponseDto(group, id);
    }

    public static GroupResponseDto of(Group group) {
        return new GroupResponseDto(group.getId(), group.getGroupName());
    }
}
