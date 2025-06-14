package com.crewManager.pro.oauth.service;


import com.crewManager.pro.config.security.JwtTokenProvider;
import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.crew.repository.CrewMemberRepository;
import com.crewManager.pro.crew.repository.CrewRepository;
import com.crewManager.pro.oauth.OAuthProvider;
import com.crewManager.pro.oauth.OAuthProviderFactory;
import com.crewManager.pro.oauth.dto.OAuthLoginRequestDto;
import com.crewManager.pro.oauth.dto.OAuthUserProfile;
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    private final CrewRepository crewRepository;

    private final CrewMemberRepository crewMemberRepository;


    private final JwtTokenProvider jwtTokenProvider;

    private final OAuthProviderFactory providerFactory;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public String login(OAuthLoginRequestDto req){

        //유형에 맞는 구현체 적용
        OAuthProvider provider = providerFactory.getProvider(req.getSocialType());

        // 토큰 생성
        String accessToken = provider.getAccessToken(req.getAuthorizationCode());
        OAuthUserProfile userProfile = provider.getUserProfile(accessToken);

        //서비스 비지니스 로직 적용
        User user = registerOrLoginUser(userProfile,req);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getAppRole());
        List<GrantedAuthority> authorities = Collections.singletonList(authority);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getId(),
                null,
                authorities
        );

        String jwtToken =  jwtTokenProvider.generateToken(authentication);
        return jwtToken;
    }

    private User registerOrLoginUser(OAuthUserProfile userProfile, OAuthLoginRequestDto req){
        // 1단계: 유저를 찾거나 새로 만들기 (크루와는 독립적인 로직)
        Optional<User> userOptional = userRepository.findByEmail(userProfile.getEmail());
        User user = userOptional.orElseGet(() -> {
            // DB에 유저가 없으면, 새로 생성합니다.
            log.info("newUser register : {}", userProfile.getEmail());
            User newUser = User.builder()
                    .email(userProfile.getEmail())
                    .name(req.getName())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();
            return userRepository.save(newUser);
        });

        // 2단계: 크루장/ 일반회원 로직 적용
        CrewMemberRole userRoleInCrew = req.getCrewMemberRole();

        if (CrewMemberRole.CREW_LEADER == userRoleInCrew) {
            // --- 크루장(CREW_LEADER)으로 가입하는 로직 ---
            String crewNameToCreate = req.getCrewName();
            log.info("CREW_LEADER create  crew : {}", crewNameToCreate);

            // 이미 같은 이름의 크루가 있는지 확인 (중복 방지)
            crewRepository.findByName(crewNameToCreate).ifPresent(c -> {
                throw new IllegalStateException("exception : exist crewName ..");
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
                    .build();
            crewMemberRepository.save(crewLeaderLink);

        } else if (CrewMemberRole.CREW_MEMBER == userRoleInCrew) {
            // --- 일반 회원(Member)으로 가입하는 로직 ---
            String crewNameToJoin = req.getCrewName(); // 프론트에서 선택한 크루의 ID를 받아야 함
            log.info("CREW_MEMBER register crewName: {}", crewNameToJoin);

            // ID로 가입할 크루를 찾음. 없으면 예외 발생.
            Crew crewToJoin = crewRepository.findByName(crewNameToJoin)
                    .orElseThrow(() -> new IllegalArgumentException("exception  : no exist crewName: " + crewNameToJoin));

            // 이미 해당 크루의 멤버인지 확인하여 중복 가입 방지
            boolean isAlreadyMember = crewMemberRepository.existsByUserAndCrew(user, crewToJoin);
            if (isAlreadyMember) {
                log.info("{} user already exist {} crew member ", user.getEmail(), crewToJoin.getName());
            } else {
                // User와 기존 Crew를 연결하는 CrewMember 정보 생성 (역할: MEMBER)
                CrewMember newMemberLink = CrewMember.builder()
                        .user(user)
                        .crew(crewToJoin)
                        .role(CrewMemberRole.CREW_MEMBER)
                        .build();
                crewMemberRepository.save(newMemberLink);
            }
        }

        return user;
    }


}
