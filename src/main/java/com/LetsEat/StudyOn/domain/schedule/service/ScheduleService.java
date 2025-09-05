package com.LetsEat.StudyOn.domain.schedule.service;

import com.LetsEat.StudyOn.common.enums.ApplicantStatus;
import com.LetsEat.StudyOn.common.enums.CollusionStatus;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.common.security.UserDetailsImpl;
import com.LetsEat.StudyOn.common.util.PageUtil;
import com.LetsEat.StudyOn.domain.schedule.dto.ScheduleRequestDto;
import com.LetsEat.StudyOn.domain.schedule.entity.Schedule;
import com.LetsEat.StudyOn.domain.schedule.entity.ScheduleUser;
import com.LetsEat.StudyOn.domain.schedule.repository.ScheduleRepository;
import com.LetsEat.StudyOn.domain.schedule.repository.ScheduleUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleUserRepository scheduleUserRepository;

    public void createSchedule(ScheduleRequestDto request) {
        request.setScheduleStatus(CollusionStatus.RECRUITING.toString());
        scheduleRepository.save(Schedule.of(request));
    }

    @Transactional
    public void updateSchedule(ScheduleRequestDto request) {
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_SCHEDULE));
        // 수정 요청한 사용자가 작성자인지 확인
        if (!request.getUser().getId().equals(schedule.getUser().getId()))
            throw new CustomException(ErrorType.CANT_UPDATE_SCHEDULE);

        Schedule.of(request);
    }

    public void applySchedule(ScheduleRequestDto request) {
        request.setSchedule(scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_SCHEDULE)));
        // 이미 지원한 공모인지 확인
        if (scheduleUserRepository.existsByScheduleAndUser(request.getSchedule(), request.getUser()))
            throw new CustomException(ErrorType.ALREADY_APPLY);

        request.setApplyStatus(ApplicantStatus.PENDING.toString());

        scheduleUserRepository.save(ScheduleUser.of(request));
    }

    @Transactional
    public void updateApplySchedule(ScheduleRequestDto request) {
        ScheduleUser scheduleUser = scheduleUserRepository.findById(request.getScheduleUserId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_APLLY));

        scheduleUser.setReason(request.getReason());
    }

    public Slice<Schedule> getScheduleList(Pageable pageable, CollusionStatus collusionStatus) {
        Slice<Schedule> scheduleUserSlice = scheduleRepository.findByScheduleStatus(collusionStatus.toString(), pageable);
        PageUtil.validateNonEmptySlice(scheduleUserSlice);
        return scheduleUserSlice;
    }

    public Schedule getScheduleDetail(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_SCHEDULE));
    }

    public Slice<ScheduleUser> getScheduleApplyList(Pageable pageable, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_SCHEDULE));
        Slice<ScheduleUser> scheduleUserSlice = scheduleUserRepository.findBySchedule(schedule, pageable);
        PageUtil.validateNonEmptySlice(scheduleUserSlice);
        return scheduleUserSlice;
    }

    @Transactional
    public void patchScheduleStatus(UserDetailsImpl userDetails, Long scheduleUserId, ApplicantStatus applicantStatus) {
        ScheduleUser scheduleUser = scheduleUserRepository.findById(scheduleUserId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_APLLY));
        if(scheduleUser.getUser().getId().equals(userDetails.getUser().getId()))
            throw new CustomException(ErrorType.CANT_UPDATE_STATUS);
        scheduleUser.setStatus(applicantStatus.toString());
    }
}