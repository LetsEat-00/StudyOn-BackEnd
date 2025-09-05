package com.LetsEat.StudyOn.domain.schedule.repository;

import com.LetsEat.StudyOn.domain.schedule.entity.Schedule;
import com.LetsEat.StudyOn.domain.schedule.entity.ScheduleUser;
import com.LetsEat.StudyOn.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUser, Long>{
    boolean existsByScheduleAndUser(Schedule schedule, User user);
    Slice<ScheduleUser> findBySchedule(Schedule schedule, Pageable pageable);
}