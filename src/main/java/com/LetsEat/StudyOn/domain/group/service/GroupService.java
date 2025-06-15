package com.LetsEat.StudyOn.domain.group.service;

import com.LetsEat.StudyOn.common.enums.MemberRole;
import com.LetsEat.StudyOn.common.exception.CustomException;
import com.LetsEat.StudyOn.common.exception.ErrorType;
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

import java.util.List;
import java.util.stream.Collectors;

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
    /**
     * 전체 그룹 목록 조회
     * @return 모든 그룹들의 정보 리스트 (그룹장 ID 포함)
     */
    public List<GroupResponseDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
                .map(group -> {
                    Long ownerId = group.getMemberList().stream()
                            .filter(member -> member != null
                                    && member.getMemberRole() == MemberRole.OWNER
                                    && member.getId() != null
                                    && member.getId().getUser() != null)
                            .findFirst()
                            .map(member -> member.getId().getUser().getId())  // null-safe
                            .orElse(null);  // 없으면 null

                    return GroupResponseDto.of(group, ownerId);
                })
                .collect(Collectors.toList());
    }
    /**
     * 특정 그룹 상세 조회
     * @param groupId 조회할 그룹 ID
     * @return 해당 그룹의 상세 정보 (그룹장 ID 포함)
     * @throws CustomException 그룹이 존재하지 않을 경우
     */
    public GroupResponseDto getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_GROUP));

        // Owner ID 추출
        Long ownerId = group.getMemberList().stream()
                .filter(m -> m.getMemberRole().name().equals("OWNER"))
                .map(m -> m.getId().getUser().getId())
                .findFirst()
                .orElse(null); // 없으면 null

        return GroupResponseDto.of(group, ownerId);
    }

    /**
     * 그룹 수정
     * @param groupId 수정할 그룹 ID
     * @param requestDto 수정할 그룹 이름과 설명
     * @param user 요청자 (로그인된 사용자)
     * @return 수정된 그룹 정보 (그룹장 ID 포함)
     * @throws CustomException 그룹이 존재하지 않거나, 권한이 없을 경우
     */
    @Transactional
    public GroupResponseDto updateGroup(Long groupId, GroupRequestDto requestDto, User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_GROUP));

        if (!group.isOwner(user)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        // 수정 내용 반영
        group.update(requestDto.getGroupName(), requestDto.getDescription());

        Long ownerId = user.getId(); // 현재 요청자 = OWNER
        return GroupResponseDto.of(group, ownerId);
    }
    /**
     * 그룹 탈퇴
     * @param groupId 탈퇴할 그룹 ID
     * @param user 요청자 (로그인된 사용자)
     * @throws CustomException 그룹이 없거나 멤버가 아니거나, 그룹장일 경우
     */
    @Transactional
    public void leaveGroup(Long groupId, User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_GROUP));

        Member member = memberRepository.findByUserAndGroup(user, group)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_GROUP_MEMBER));
        log.info("탈퇴할 멤버 ID: {}", member.getId());
        log.info("현재 요청 유저 ID: {}", user.getId());
        log.info("해당 멤버의 역할: {}", member.getMemberRole());

        if (member.getMemberRole() == MemberRole.OWNER) {
            throw new CustomException(ErrorType.GROUP_OWNER_CANNOT_LEAVE);
        }

        memberRepository.delete(member);
    }
    /**
     * 그룹 삭제
     * @param groupId 삭제할 그룹 ID
     * @param user 요청자 (로그인된 사용자)
     * @throws CustomException 그룹이 존재하지 않거나, 요청자가 그룹장이 아닐 경우
     */
    @Transactional
    public void deleteGroup(Long groupId, User user) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_GROUP));

        // 그룹장 확인
        if (!group.isOwner(user)) {
            throw new CustomException(ErrorType.NO_AUTHENTICATION);
        }

        groupRepository.delete(group);
    }

}
