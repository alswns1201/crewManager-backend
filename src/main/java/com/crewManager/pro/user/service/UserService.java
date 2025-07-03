package com.crewManager.pro.user.service;

import com.crewManager.pro.auth.dto.OAuthLoginRequestDto;
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

    // [신규] 사용자 '생성' 전용 메소드
    @Transactional
    public User createUser(String email, OAuthLoginRequestDto req) {
        // 중복 가입 방지 체크 (중요!)
        return userRepository.findByEmail(email)
                .orElseGet(()->{
                    if(req.getName() == null || req.getName().isBlank()){
                        throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
                    }
                    User newUser = User.builder()
                            .email(email)
                            .name(req.getName())
                            .phoneNumber(req.getPhoneNumber())
                            .appRole(AppRole.GENERAL)
                            .build();
                    return userRepository.save(newUser);
                });


    }

    // [신규] 사용자 '조회' 전용 메소드
    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND)); // 로그인 시도인데 회원이 없으면 에러
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