package com.crewManager.pro.auth.service;

import com.crewManager.pro.auth.OAuthProvider;
import com.crewManager.pro.auth.OAuthProviderFactory;
import com.crewManager.pro.auth.dto.OAuthLoginRequestDto;
import com.crewManager.pro.auth.dto.OAuthUserProfile;
import com.crewManager.pro.config.security.JwtTokenProvider;
import com.crewManager.pro.crew.service.CrewService; // CrewService import
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.service.UserService; // UserService import
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService; // UserService 의존성 주입
    private final CrewService crewService; // CrewService 의존성 주입
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthProviderFactory providerFactory;

    /**
     * 소셜 로그인의 전체 흐름을 담당합니다.
     * 트랜잭션은 실제 DB 작업이 필요한 내부 메서드에서 시작됩니다.
     * @param req 로그인 요청 DTO
     * @return JWT 토큰
     */
    public String login(OAuthLoginRequestDto req) {
        // 1. 외부 OAuth Provider로부터 사용자 프로필 정보 가져오기 (트랜잭션 바깥)
        OAuthProvider provider = providerFactory.getProvider(req.getSocialType());
        String accessToken = provider.getAccessToken(req.getAuthorizationCode());
        OAuthUserProfile userProfile = provider.getUserProfile(accessToken);

        // 2. 사용자 등록/로그인 및 크루 가입/생성 처리 (트랜잭션 내부)
        User user = processUserAndCrewRegistration(userProfile, req);

        // 3. Spring Security용 Authentication 객체 생성
        Authentication authentication = createAuthentication(user);

        // 4. JWT 토큰 생성 및 반환 (트랜잭션 바깥)
        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * 사용자 및 크루 관련 DB 작업을 하나의 트랜잭션으로 묶어 처리합니다.
     * @param userProfile 소셜 프로필
     * @param req 로그인 요청 DTO
     * @return 최종적으로 등록/로그인된 User 객체
     */
    @Transactional
    public User processUserAndCrewRegistration(OAuthUserProfile userProfile, OAuthLoginRequestDto req) {
        // UserService를 통해 사용자를 찾거나 새로 생성합니다.
        User user = userService.findOrCreateUser(userProfile.getEmail(), req.getName());

        // CrewService를 통해 크루 멤버십 관련 로직을 처리합니다.
        crewService.registerCrewMember(user, req.getCrewName(), req.getCrewMemberRole());

        return user;
    }

    /**
     * 인증된 사용자에 대한 Spring Security Authentication 객체를 생성합니다.
     * @param user 인증된 사용자
     * @return Authentication 객체
     */
    private Authentication createAuthentication(User user) {
        // 사용자 ID를 principal로 사용합니다.
        // AppRole에 따라 권한을 부여합니다. (필요 시 수정)
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getAppRole());
        List<GrantedAuthority> authorities = Collections.singletonList(authority);

        return new UsernamePasswordAuthenticationToken(
                user.getId(), // Principal: 사용자 ID
                null,       // Credentials: 비밀번호는 사용하지 않음
                authorities // Authorities: 권한 목록
        );
    }
}