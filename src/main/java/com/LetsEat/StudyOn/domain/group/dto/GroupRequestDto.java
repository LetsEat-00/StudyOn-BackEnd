package com.LetsEat.StudyOn.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDto {

    @NotBlank(message = "그룹 이름은 공백일 수 없습니다.")
    @Size(min = 2, max = 15, message = "그룹 이름은 최소 2자 이상, 최대 15자 이하이어야 합니다.")
    private String groupName;

    @NotBlank(message = "그룹 소개는 공백일 수 없습니다.")
    @Size(min = 2, max = 50, message = "그룹 소개는 최소 2자 이상, 최대 50자 이하이어야 합니다.")
    private String description;
}
