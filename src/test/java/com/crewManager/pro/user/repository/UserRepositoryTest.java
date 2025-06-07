package com.crewManager.pro.user.repository;


import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.crew.domain.CrewMemberStatus;
import com.crewManager.pro.crew.repository.CrewRepository;
import com.crewManager.pro.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Test
    @DisplayName("유저가 크루를 등록하는 테스트")
    @Commit// h2에서 확인
    public void createTest(){

        //given
        //1, 유저 생성
        User user = User.builder()
                .name("화이팅")
                .password("1234")
                .phoneNumber("01040651205")
                .build();
        userRepository.save(user);

        //.2 유저가 크루 만듬
        Crew newCrew = Crew.builder()
                .name("springboot 크루")
                .description("화이팅을 위한 스터디")
                .build();

        // 3. 연관관계 설정
        CrewMember crewMember = CrewMember.builder()
                .user(user)
                .crew(newCrew)
                .role(CrewMemberRole.CREW_LEADER)
                .status(CrewMemberStatus.ACTIVE)
                .build();

        crewMember.setupCrewAndUser();

        Crew savedCrew = crewRepository.save(newCrew);

        // 1. 저장된 Crew를 다시 조회한다.
        Crew foundCrew = crewRepository.findById(newCrew.getId())
                .orElseThrow(() -> new AssertionError("저장된 크루를 찾을 수 없습니다."));
        User foundLeader = foundCrew.findCrewLeader()
                .orElseThrow(() -> new AssertionError("findCrewLeader() 메서드가 크루장을 찾지 못했습니다."));

        assertEquals(user.getId(), foundLeader.getId(), "크루장의 ID가 일치하지 않습니다.");
        assertEquals(user.getName(), foundLeader.getName(), "크루장의 이름이 일치하지 않습니다.");

        User foundUser = userRepository.findById(user.getId()).orElseThrow();
        assertFalse(foundUser.getCrewMembers().isEmpty(), "User 엔티티에 CrewMember 정보가 없습니다.");
        assertEquals(1, foundUser.getCrewMembers().size(), "User가 가진 멤버십 개수가 1이 아닙니다.");


    }

}
