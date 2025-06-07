package com.crewManager.pro.user.domain;


import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@ToString(exclude = "crewMembers") // .toString 시 순환 참조 방지
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CrewMember> crewMembers = new ArrayList<>();

    //  Spring Security의 PasswordEncoder 사용 예정.
    private String password;

    private String name;

    private String phoneNumber;

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

}
