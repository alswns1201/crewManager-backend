package com.crewManager.pro.crew.domain;


import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "crew_members", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_crew_member_user_crew", // 제약 조건 이름
                columnNames = {"user_id", "crew_id"} // 유저 한 명은 한 크루에 한 번만 속할 수 있도록 DB 제약 설정
        )
})
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id", nullable = false)
    private Crew crew;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrewMemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrewMemberStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt; // 가입 날짜 (필수)

    @Column(nullable = false) // 업데이트 날짜 (필수)
    private LocalDateTime updatedAt;

    @Builder //강제성을 부여하기 위해 적용.
    public CrewMember(User user, Crew crew, CrewMemberRole role, CrewMemberStatus status) {
        this.user = user;
        this.crew = crew;
        this.role = role;
        this.status = status;
    }

    public void setupCrewAndUser() {
        if(this.user != null) {
            this.user.getCrewMembers().add(this);
        }
        if(this.crew != null) {
            this.crew.getCrewMembers().add(this);
        }
    }

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
