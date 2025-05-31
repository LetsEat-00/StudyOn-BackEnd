package com.LetsEat.StudyOn.domain.user.repository;

import com.LetsEat.StudyOn.domain.user.entity.Follow;
import com.LetsEat.StudyOn.domain.user.entity.FollowId;
import com.LetsEat.StudyOn.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);
}