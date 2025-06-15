package com.crewManager.pro.auth.service;


import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.crew.repository.CrewMemberRepository;
import com.crewManager.pro.crew.repository.CrewRepository;
import com.crewManager.pro.auth.SocialType;
import com.crewManager.pro.auth.dto.OAuthLoginRequestDto;
import com.crewManager.pro.auth.dto.OAuthUserProfile;
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Test
    @DisplayName("registerOrLoginUser 테스트")
    public void registerOrLoginUserTest(){

        //given
        OAuthUserProfile fakeProfile = new OAuthUserProfile();
        //소셜에서 받아온 값을 가공
        fakeProfile.setEmail("integration-test@exam.com");
        fakeProfile.setProviderId("kakao-1234");
        // 화면 입력 값 들 가공
        OAuthLoginRequestDto requestDto = new OAuthLoginRequestDto();
        requestDto.setName("김러너");
        requestDto.setCrewName("만석을 좋아하는 러너");
        requestDto.setCrewMemberRole(CrewMemberRole.CREW_LEADER);
        requestDto.setSocialType(SocialType.KAKAO);

        //when
        User result = authService.registerOrLoginUser(fakeProfile,requestDto);

        //then
        User foundUser =  userRepository.findById(result.getId())
                .orElseThrow();
        //들어간 이메일이 잘 들어 갔는가
        assertThat(foundUser.getEmail()).isEqualTo("integration-test@exam.com");
        // 크루가 만들어 졌는가
        Crew foundCrew = crewRepository.findByName("만석을 좋아하는 러너").orElseThrow();
        assertThat(foundCrew).isNotNull();
        // 인원이 1명 모임장인가.
        List<CrewMember> members = crewMemberRepository.findAllByCrew(foundCrew);
        assertThat(members).hasSize(1);
        assertThat(members.get(0).getRole()).isEqualTo(CrewMemberRole.CREW_LEADER);

    }

}
