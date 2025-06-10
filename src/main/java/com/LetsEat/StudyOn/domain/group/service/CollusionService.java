package com.LetsEat.StudyOn.domain.group.service;

import com.LetsEat.StudyOn.common.enums.ApplicantStatus;
import com.LetsEat.StudyOn.common.enums.CollusionStatus;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.common.util.PageUtil;
import com.LetsEat.StudyOn.domain.group.dto.*;
import com.LetsEat.StudyOn.domain.group.entity.Applicant;
import com.LetsEat.StudyOn.domain.group.entity.ApplicantId;
import com.LetsEat.StudyOn.domain.group.entity.Collusion;
import com.LetsEat.StudyOn.domain.group.entity.Group;
import com.LetsEat.StudyOn.domain.group.repository.ApplicantRepository;
import com.LetsEat.StudyOn.domain.group.repository.CollusionRepository;
import com.LetsEat.StudyOn.domain.group.repository.GroupRepository;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CollusionService {

    private final CollusionRepository collusionRepository;
    private final GroupRepository groupRepository;
    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;

    /**
     * 그룹 공고 생성
     * @param requestDto 그룹 공고 정보
     * @param user 사용자
     * @return 그룹 공고 정보
     */
    @Transactional
    public CollusionResponseDto createCollusion(CollusionRequestDto requestDto, User user) {
        Group group = groupRepository.findById(requestDto.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_GROUP));

        // 그룹의 owner 인지 확인
        if (!group.isOwner(user)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        Collusion collusion = Collusion.of(requestDto, group, user);

        return CollusionResponseDto.of(collusionRepository.save(collusion));
    }

    /**
     * 그룹 공고 수정
     * @param collusionId 그룹 공고 ID
     * @param requestDto 그룹 공고 정보
     * @param user 사용자
     * @return 그룹 공고 정보
     */
    @Transactional
    public CollusionResponseDto updateCollusion(Long collusionId, CollusionUpdateRequestDto requestDto, User user) {
        Collusion collusion = collusionRepository.findById(collusionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COLLUSION));

        // 그룹의 owner 인지 확인
        if (!collusion.getGroup().isOwner(user)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        collusion.updateCollusion(requestDto);

        return CollusionResponseDto.of(collusionRepository.save(collusion));
    }

    /**
     * 그룹 공고 상세조회
     * @param collusionId 그룹 공고 ID
     * @return 그룹 공고 정보
     */
    public CollusionResponseDto getCollusion(Long collusionId) {
        Collusion collusion = collusionRepository.findById(collusionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COLLUSION));

        return CollusionResponseDto.of(collusion);
    }

    /**
     * 상태별 그룹 공고 목록 조회
     * @param pageable 페이징
     * @param collusionStatus 그룹 공고 상태
     * @return 그룹 공고 목록
     */
    public Slice<CollusionResponseDto> getCollusionList(Pageable pageable, CollusionStatus collusionStatus) {
        Slice<CollusionResponseDto> collusionList;

        if (collusionStatus == CollusionStatus.ALL) {
            // 조회불가(HIDDEN)을 제외한 모든 상태 그룹 공고 조회
            collusionList = collusionRepository.findByCollusionStatusNot(collusionStatus, pageable)
                    .map(CollusionResponseDto::of);
        }else {
            // 특정 상태의 그룹 공고 조회
            collusionList = collusionRepository.findByCollusionStatus(collusionStatus, pageable)
                    .map(CollusionResponseDto::of);
        }
        // 페이지 요소 검증
        PageUtil.validateNonEmptySlice(collusionList);

        return collusionList;
    }

    /**
     * 그룹 공고 지원
     * @param collusionId 그룹 공고 ID
     * @param applicantRequestDto 지원사유
     * @param user 사용자
     */
    @Transactional
    public void createApplicant(Long collusionId, ApplicantRequestDto applicantRequestDto, User user) {
        Collusion collusion = collusionRepository.findById(collusionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COLLUSION));

        // 그룹 공고 상태 확인 (지원 불가 시 예외처리)
        collusion.collusionStatusCheck();

        // 이미 속한 그룹인지 확인
        if (collusion.getGroup().isMember(user)) {
            throw new CustomException(ErrorType.ALREADY_GROUP);
        }

        // 이미 지원한 그룹 공고인지 확인
        if (applicantRepository.existsByIdUserAndIdCollusion(user, collusion)) {
            throw new CustomException(ErrorType.ALREADY_APPLIED_COLLUSION);
        }else {
            Applicant applicant = Applicant.of(applicantRequestDto.getReason(), collusion, user);
            applicantRepository.save(applicant);
        }
    }

    /**
     * 그룹 공고 지원자 목록 조회
     * @param collusionId 그룹 공고 ID
     * @param applicantStatus 지원상태
     * @param pageable 페이징
     * @param user 사용자
     * @return 그룹 공고 지원자 목록
     */
    public Slice<ApplicantResponseDto> getApplicantList(Long collusionId, ApplicantStatus applicantStatus, Pageable pageable, User user) {
        Collusion collusion = collusionRepository.findById(collusionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COLLUSION));

        // 그룹의 owner 인지 확인
        if (!collusion.getGroup().isOwner(user)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        // 상태별 지원자 목록 조회
        Slice<ApplicantResponseDto> applicantList = applicantRepository
                .findByIdCollusionIdAndApplicantStatus(collusionId, applicantStatus, pageable)
                .map(ApplicantResponseDto::of);

        // 페이지 요소 검증
        PageUtil.validateNonEmptySlice(applicantList);

        return applicantList;
    }

    /**
     * 그룹 공고 지원 승인/거절
     * @param collusionId 그룹 공고 ID
     * @param userId 그룹 공고 지원자
     * @param applicantStatus 지원자 상태
     * @param user 사용자
     * @return 그룹 공고 승인/거절 응답메시지
     */
    @Transactional
    public String updateApplicants(Long collusionId, Long userId, ApplicantStatus applicantStatus, User user) {
        Collusion collusion = collusionRepository.findById(collusionId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_COLLUSION));

        // 그룹의 owner 인지 확인
        if (!collusion.getGroup().isOwner(user)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        // 지원자 조회
        User appliedUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        // 그룹 공고 지원 현황 조회
        ApplicantId applicantId = new ApplicantId(appliedUser, collusion);
        Applicant applicant = applicantRepository.findById(applicantId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_APPLICANT));

        String message;
        switch (applicantStatus) {
            case ACCEPTED:
                // 그룹 멤버에 추가
                Group group = collusion.getGroup();
                if (!group.isMember(appliedUser)) {
                    group.addMember(appliedUser);
                }
                message = "그룹 공고 지원 승인 성공";
                break;

            case REJECTED:
                message = "그룹 공고 지원 거절 성공";
                break;

            default:
                throw new CustomException(ErrorType.INVALID_APPLICANT_STATUS);
        }

        // 그룹 공고 지원자 상태 변경
        applicant.updateApplicantStatus(applicantStatus);

        return message;
    }
}
