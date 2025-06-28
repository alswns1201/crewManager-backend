package com.crewManager.pro.crew.service;

import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.crew.domain.CrewMemberStatus;
import com.crewManager.pro.crew.dto.CrewResponseDto;
import com.crewManager.pro.crew.repository.CrewMemberRepository;
import com.crewManager.pro.crew.repository.CrewRepository;
import com.crewManager.pro.exception.BusinessException;
import com.crewManager.pro.exception.ErrorCode;
import com.crewManager.pro.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrewService {

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;

    /**
     * 사용자의 역할에 따라 크루를 생성하거나 기존 크루에 가입시킵니다.
     * @param user 가입할 사용자
     * @param crewName 생성 또는 가입할 크루의 이름
     * @param role 사용자의 역할 (크루장/멤버)
     */
    @Transactional
    public void registerCrewMember(User user, String crewName, CrewMemberRole role) {
        if (crewName == null || crewName.isBlank()) {
            throw new BusinessException(ErrorCode.CREW_NOT_FOUND);
        }

        if (role == null) {
            // 역할이 지정되지 않은 경우, 아무 작업도 하지 않고 종료
            log.info("User {} has no role specified for crew registration. Skipping.", user.getEmail());
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (CrewMemberRole.CREW_LEADER == role) {
            createCrewAndAddLeader(user, crewName);
        } else if (CrewMemberRole.CREW_MEMBER == role) {
            joinExistingCrew(user, crewName);
        }
    }

    private void createCrewAndAddLeader(User user, String crewNameToCreate) {
        log.info("Attempting to create crew '{}' with leader '{}'", crewNameToCreate, user.getEmail());

        // 이미 같은 이름의 크루가 있는지 확인 (중복 방지)
        crewRepository.findByName(crewNameToCreate).ifPresent(c -> {
            throw new BusinessException(ErrorCode.CREW_NAME_DUPLICATED);
        });

        // 새 크루 생성
        Crew newCrew = Crew.builder()
                .name(crewNameToCreate)
                .build();
        crewRepository.save(newCrew);

        // User와 새 Crew를 연결하는 CrewMember 정보 생성 (역할: LEADER)
        CrewMember crewLeaderLink = CrewMember.builder()
                .user(user)
                .crew(newCrew)
                .role(CrewMemberRole.CREW_LEADER)
                .status(CrewMemberStatus.ACTIVE)
                .build();
        crewMemberRepository.save(crewLeaderLink);
        log.info("Successfully created crew '{}' and registered leader '{}'", crewNameToCreate, user.getEmail());
    }

    private void joinExistingCrew(User user, String crewNameToJoin) {
        log.info("User '{}' attempting to join crew '{}'", user.getEmail(), crewNameToJoin);

        // 이름으로 가입할 크루를 찾음. 없으면 예외 발생.
        Crew crewToJoin = crewRepository.findByName(crewNameToJoin)
                .orElseThrow(() -> new BusinessException(ErrorCode.CREW_NOT_FOUND));

        // 이미 해당 크루의 멤버인지 확인하여 중복 가입 방지
        if (crewMemberRepository.existsByUserAndCrew(user, crewToJoin)) {
            throw new BusinessException(ErrorCode.CREW_MEMBER_DUPLICATED);
        }

        // User와 기존 Crew를 연결하는 CrewMember 정보 생성 (역할: MEMBER)
        CrewMember newMemberLink = CrewMember.builder()
                .user(user)
                .crew(crewToJoin)
                .role(CrewMemberRole.CREW_MEMBER)
                .status(CrewMemberStatus.ACTIVE) // 가입 승인 대기 상태로 시작 (선택)
                .build();
        crewMemberRepository.save(newMemberLink);
        log.info("Successfully registered user '{}' to crew '{}'", user.getEmail(), crewNameToJoin);
    }

    // 이전에 만들었던 전체 크루 조회 API 로직
    public List<CrewResponseDto> findAll() {
        return crewRepository.findAll().stream()
                .map(CrewResponseDto::new)// dto로 변환해서 원하는 값만 보여주기.
                .collect(Collectors.toList());
    }
}