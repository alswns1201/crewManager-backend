package com.crewManager.pro.user.service;

import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일을 기반으로 사용자를 찾거나, 존재하지 않으면 새로 생성하여 반환합니다.
     * 이 메서드는 독립적인 트랜잭션 내에서 실행될 수도 있고,
     * 다른 서비스에 의해 호출되어 기존 트랜잭션에 참여할 수도 있습니다.
     * @param email 사용자 이메일
     * @param name 사용자 이름 (신규 가입 시 사용)
     * @return 찾거나 새로 생성한 User 객체
     */
    @Transactional
    public User findOrCreateUser(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("New user registration: {}", email);
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            // .appRole(UserRole.USER) // 기본 역할 설정이 필요하다면 여기에 추가
                            .build();
                    return userRepository.save(newUser);
                });
    }
}