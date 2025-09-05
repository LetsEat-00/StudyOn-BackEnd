package com.LetsEat.StudyOn.domain.group.service;

import com.LetsEat.StudyOn.common.enums.MemberRole;
import com.LetsEat.StudyOn.domain.group.dto.GroupRequestDto;
import com.LetsEat.StudyOn.domain.group.dto.GroupResponseDto;
import com.LetsEat.StudyOn.domain.group.entity.Group;
import com.LetsEat.StudyOn.domain.group.entity.Member;
import com.LetsEat.StudyOn.domain.group.repository.GroupRepository;
import com.LetsEat.StudyOn.domain.group.repository.MemberRepository;
import com.LetsEat.StudyOn.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    /**
     * 그룹 생성
     * @param requestDto 그룹 이름, 그룹 소개
     * @param user 사용자
     * @return 그룹 정보, 그룹 생성자 ID
     */
    @Transactional
    public GroupResponseDto createGroup(GroupRequestDto requestDto, User user) {
        // 그룹 생성
        Group group = Group.of(requestDto);
        groupRepository.save(group);

        // 그룹 멤버(소유자) 생성
        Member member = Member.of(user, group, MemberRole.OWNER);
        memberRepository.save(member);

        return GroupResponseDto.of(group, user.getId());
    }
}
