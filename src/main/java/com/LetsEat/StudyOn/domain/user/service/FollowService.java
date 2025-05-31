package com.LetsEat.StudyOn.domain.user.service;

import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
import com.LetsEat.StudyOn.domain.user.entity.Follow;
import com.LetsEat.StudyOn.domain.user.entity.FollowId;
import com.LetsEat.StudyOn.domain.user.entity.User;
import com.LetsEat.StudyOn.domain.user.repository.FollowRepository;
import com.LetsEat.StudyOn.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로우
    public void follow(Long followerId, Long followingId) {
        // 팔로우 팔로잉 동일 체크
        if (followerId.equals(followingId)) {
            throw new CustomException(ErrorType.CANT_FOLLOW_YOURSELF);
        }
        // 실제 사용자인지 체크
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_JWT));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
        // 이미 팔로우 했는지 체크
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new CustomException(ErrorType.ALREADY_FOLLOWED);
        }

        Follow follow = Follow.builder()
                .id(new FollowId(follower.getId(), following.getId()))
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    // 언팔로우
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        // 실제 사용자인지 체크
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_JWT));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));
        // 이미 언팔로우 했는지 체크
        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new CustomException(ErrorType.ALREADY_UNFOLLOWED);
        }

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    // 팔로잉 목록 조회
    public List<User> getFollowingList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        return followRepository.findByFollower(user)
                .stream()
                .map(Follow::getFollowing)
                .toList();
    }

    // 팔로워 목록 조회
    public List<User> getFollowerList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        return followRepository.findByFollowing(user)
                .stream()
                .map(Follow::getFollower)
                .toList();
    }
}