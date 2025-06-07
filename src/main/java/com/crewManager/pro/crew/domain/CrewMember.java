package com.crewManager.pro.crew.domain;


import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@IdClass(CrewMemberId.class)
public class CrewMember {

//    @EmbeddedId  : @IdClass(CrewMemberId.class) 사용시 제거.
//    private CrewMemberId crewMemberId;

    @Id // <--- 첫 번째 복합 키 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id // <--- 두 번째 복합 키 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    private CrewMemberRole crewMemberRole;

    private CrewMemberStatus crewMemberStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt; // 가입 날짜 (필수)

    @Column(nullable = false) // 업데이트 날짜 (필수)
    private LocalDateTime updatedAt;

    // @PrePersist 및 @PreUpdate 콜백 메서드 추가
    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }



}
