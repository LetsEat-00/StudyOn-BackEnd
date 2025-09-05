package com.LetsEat.StudyOn.domain.group.repository;

import com.LetsEat.StudyOn.domain.group.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
