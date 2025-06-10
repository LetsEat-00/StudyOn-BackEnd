package com.LetsEat.StudyOn.domain.group.repository;

import com.LetsEat.StudyOn.common.enums.ApplicantStatus;
import com.LetsEat.StudyOn.domain.group.entity.Applicant;
import com.LetsEat.StudyOn.domain.group.entity.ApplicantId;
import com.LetsEat.StudyOn.domain.group.entity.Collusion;
import com.LetsEat.StudyOn.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    boolean existsByIdUserAndIdCollusion(User user, Collusion collusion);

    Slice<Applicant> findByIdCollusionIdAndApplicantStatus(Long collusionId, ApplicantStatus applicantStatus, Pageable pageable);

    Optional<Applicant> findById(ApplicantId applicantId);
}
