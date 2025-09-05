package com.LetsEat.StudyOn.domain.schedule.repository;

import com.LetsEat.StudyOn.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Slice<Schedule> findByScheduleStatus(String scheduleStatus, Pageable pageable);
}
