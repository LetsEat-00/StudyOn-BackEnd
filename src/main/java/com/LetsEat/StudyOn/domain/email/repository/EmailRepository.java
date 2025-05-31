package com.LetsEat.StudyOn.domain.email.repository;

import com.LetsEat.StudyOn.domain.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    boolean existsByEmail(String email);

    Optional<Email> findByEmail(String email);
}
