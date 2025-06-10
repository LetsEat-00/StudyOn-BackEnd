package com.LetsEat.StudyOn.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantRequestDto {

    @NotBlank(message = "지원사유는 필수입니다.")
    @Size(min = 10, max = 200, message = "지원사유는 10자 이상 200자 이하로 입력해주세요.")
    private String reason;
}
