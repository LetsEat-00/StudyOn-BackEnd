package com.LetsEat.StudyOn.domain.group.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollusionUpdateRequestDto {

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(min = 3, max = 30, message = "제목은 최소 3자 이상, 최대 30자 이하이어야 합니다.")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Size(min = 10, message = "내용은 최소 10자 이상이어야 합니다.")
    private String content;

    @NotBlank(message = "그룹 공고 상태는 공백일 수 없습니다.")
    @Pattern(regexp = "RECRUITING|CLOSED|EXPIRED|HIDDEN",
            message = "잘못된 그룹 공고 상태입니다. (RECRUITING, CLOSED, EXPIRED, HIDDEN) 중 하나여야 합니다.")
    private String collusionStatus;

    @NotBlank(message = "카테고리는 공백일 수 없습니다.")
    private String category;

    private String tags;

    @NotNull(message = "모집 인원은 공백일 수 없습니다.")
    @Min(value = 1, message = "모집 인원은 최소 1 이상이어야 합니다.")
    private int maxMembers;

    @NotNull(message = "시작일시는 공백일 수 없습니다.")
    @FutureOrPresent(message = "시작일시는 현재 또는 미래이어야 합니다.")
    private LocalDateTime startAt;

    @NotNull(message = "종료일시는 공백일 수 없습니다.")
    @Future(message = "종료일시는 미래이어야 합니다.")
    private LocalDateTime endAt;
}
