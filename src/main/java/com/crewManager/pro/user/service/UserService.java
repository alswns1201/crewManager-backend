package com.crewManager.pro.user.service;

import com.crewManager.pro.exception.BusinessException;
import com.crewManager.pro.exception.ErrorCode;
import com.crewManager.pro.user.AppRole;
import com.crewManager.pro.user.domain.User;
import com.crewManager.pro.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 클래스 레벨에서는 기본적으로 읽기 전용 트랜잭션
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
    @Transactional // 쓰기 작업이므로 개별적으로 @Transactional 어노테이션을 붙여 readOnly=false를 적용
    public User findOrCreateUser(String email, String name) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    log.info("새로운 사용자 등록을 시작합니다. Email: {}", email);
                    if(name== null){throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);}
                    User newUser = User.builder()
                            .id(UUID.randomUUID().toString()) // UUID를 직접 생성하여 설정
                            .email(email)
                            .name(name)
                            // 소셜 로그인이므로 실제 비밀번호는 의미가 없습니다. 임의의 값으로 설정합니다.
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .appRole(AppRole.GENERAL) // 기본 역할 설정
                            .build();
                    return userRepository.save(newUser);
                });
    }

    /**
     * [새로 추가] 사용자 ID를 기반으로 사용자를 찾아서 반환합니다.
     * 사용자가 존재하지 않을 경우 EntityNotFoundException 예외를 발생시킵니다.
     * @param userId 찾을 사용자의 ID
     * @return 찾아낸 User 객체
     * @throws EntityNotFoundException 사용자를 찾지 못한 경우
     */
    public User findById(String userId) { // ID 타입이 Long이라면 Long으로 받습니다.
        log.debug("ID로 사용자 조회를 시도합니다. User ID: {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다. User ID: {}", userId);
                    return new EntityNotFoundException("ID가 " + userId + "인 사용자를 찾을 수 없습니다.");
                });
    }


}