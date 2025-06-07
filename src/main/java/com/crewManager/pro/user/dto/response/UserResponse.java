package com.crewManager.pro.user.dto.response;

import com.crewManager.pro.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private String id;          // 사용자의 고유 ID (UUID)
    private String name;        // 사용자 이름
    private String phoneNumber; // 사용자 전화번호
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 업데이트 시간

    // User 엔티티를 UserResponse DTO로 변환하는 정적 팩토리 메서드
    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
