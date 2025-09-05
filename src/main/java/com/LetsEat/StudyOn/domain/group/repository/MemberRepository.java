package com.LetsEat.StudyOn.domain.group.repository;

import com.LetsEat.StudyOn.domain.group.entity.Group;
import com.LetsEat.StudyOn.domain.group.entity.Member;
import com.LetsEat.StudyOn.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //특정 사용자와 그룹에 해당하는 멤버 조회
    Optional<Member> findByUserAndGroup(User user, Group group);
}
