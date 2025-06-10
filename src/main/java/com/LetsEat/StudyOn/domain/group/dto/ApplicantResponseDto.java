package com.LetsEat.StudyOn.domain.group.dto;

import com.LetsEat.StudyOn.domain.group.entity.Applicant;
import lombok.Getter;

@Getter
public class ApplicantResponseDto {

    private final Long collusionId;
    private final Long ApplicantId;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final String applicantStatus;
    private final String reason;

    private ApplicantResponseDto(Applicant applicant) {
        this.collusionId = applicant.getId().getCollusion().getId();
        this.ApplicantId = applicant.getId().getUser().getId();
        this.email = applicant.getId().getUser().getEmail();
        this.nickname = applicant.getId().getUser().getNickname();
        this.profileImage = applicant.getId().getUser().getProfileImage();
        this.applicantStatus = applicant.getApplicantStatus().toString();
        this.reason = applicant.getReason();
    }

    public static ApplicantResponseDto of(Applicant applicant) {
        return new ApplicantResponseDto(applicant);
    }
}
