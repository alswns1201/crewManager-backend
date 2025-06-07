package com.crewManager.pro.crew.domain;

import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "crews")
@ToString(exclude = "crewMembers") // 순환 참조 방지
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false, unique = true) // 크루 이름은 유니크해야 함
    private String name;

    @Column(length = 500) // 설명은 길 수 있으므로 길이 제한
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CrewStatus status = CrewStatus.PENDING_APPROVAL;

    @OneToMany(mappedBy = "crew" ,cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CrewMember> crewMembers = new ArrayList<>();



    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Optional<User> findCrewLeader() {
        return this.crewMembers.stream()
                .filter(member -> member.getRole() == CrewMemberRole.CREW_LEADER)
                .map(CrewMember::getUser)
                .findFirst();
    }


}
