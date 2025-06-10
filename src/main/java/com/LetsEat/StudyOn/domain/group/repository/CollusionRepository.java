package com.LetsEat.StudyOn.domain.group.repository;

import com.LetsEat.StudyOn.common.enums.CollusionStatus;
import com.LetsEat.StudyOn.domain.group.entity.Collusion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollusionRepository extends JpaRepository<Collusion, Long> {

    // 특정 그룹 공고 상태만 조회
    Slice<Collusion> findByCollusionStatus(CollusionStatus collusionStatus, Pageable pageable);

    // 조회불가 상태를 제외한 모든 상태 그룹 공고 조회
    Slice<Collusion> findByCollusionStatusNot(CollusionStatus collusionStatus, Pageable pageable);
}
