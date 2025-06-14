package com.crewManager.pro.auth.service;


import com.crewManager.pro.config.security.JwtTokenProvider;
import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.crew.repository.CrewMemberRepository;
import com.crewManager.pro.crew.repository.CrewRepository;
import com.crewManager.pro.oauth.OAuthProvider;
import com.crewManager.pro.oauth.OAuthProviderFactory;
import com.crewManager.pro.oauth.SocialType;
import com.crewManager.pro.oauth.dto.OAuthLoginRequestDto;
import com.crewManager.pro.oauth.dto.OAuthUserProfile;
import com.crewManager.pro.oauth.service.AuthService;
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.mockito.Mockito; // Mockito 클래스를 임포트

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    // --- 가짜(Mock) 객체들 ---
    @Mock private UserRepository userRepository;
    @Mock private CrewRepository crewRepository;
    @Mock private CrewMemberRepository crewMemberRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private OAuthProviderFactory providerFactory;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private OAuthProvider mockOAuthProvider; // 특정 소셜 플랫폼 Provider의 가짜 객체

    @Test
    @DisplayName("신규 유저가 크루장으로 등록 test")
    public void login_user_leader(){
        //given(화면)
        OAuthLoginRequestDto requestDto = new OAuthLoginRequestDto();
        requestDto.setSocialType(SocialType.KAKAO);// 카카오 로그인 접근
        requestDto.setAuthorizationCode("test-auth-code");
        requestDto.setCrewMemberRole(CrewMemberRole.CREW_LEADER); // "CREW_LEADER"
        requestDto.setCrewName("만석이를 좋아하는 러너들");
        requestDto.setName("김러너");

        //when : @mock에 대해서 행동 규칙 적용 .
        Mockito.when(providerFactory.getProvider(SocialType.KAKAO)).thenReturn(mockOAuthProvider);
        Mockito.when(mockOAuthProvider.getAccessToken("test-auth-code")).thenReturn("test-access-token");
        OAuthUserProfile fakeProfile = new OAuthUserProfile();
        fakeProfile.setEmail("newuser@example.com");
        Mockito.when(mockOAuthProvider.getUserProfile("test-access-token")).thenReturn(fakeProfile);

        // - UserRepository가 "newuser@example.com"을 찾으면 없다고(Optional.empty()) 응답하도록 설정해서 해당 이메일 넣을수 있게 설정.
        Mockito.when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        // - save 메서드가 호출되면, 받은 User 객체를 그대로 반환하도록 설정
        Mockito.when(crewRepository.findByName("만석이를 좋아하는 러너들")).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(crewRepository.save(Mockito.any(Crew.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // - JWT Provider가 "fake-jwt-token"을 반환하도록 설정
        Mockito.when(jwtTokenProvider.generateToken(Mockito.any())).thenReturn("fake-jwt-token");

        //실제 when
        String resultToken = authService.login(requestDto);
        //실제로 if 조건을 잘 타서 크루장안에 있는 findByName을 잘 타는가.
        Mockito.verify(crewRepository, Mockito.times(1)).findByName("만석이를 좋아하는 러너들");
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(crewRepository, Mockito.times(1)).save(Mockito.any(Crew.class));

        assertThat(resultToken).isEqualTo("fake-jwt-token");


    }



}
