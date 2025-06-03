package com.crewManager.pro.user.domain;


import com.crewManager.pro.crew.domain.Crew;
import com.crewManager.pro.crew.domain.CrewMember;
import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false)
    private String password;

    private String name;

    private String phoneNumber;

    @Builder.Default
    @Column(nullable = false)
    private UserRole role = UserRole.ROLE_USER;

    // user 정보가 Crew에도 반영되어야 함.즉 userLeader 변경 가능성 염두.
    @OneToMany(mappedBy = "leaderUser",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Crew> createCrews = new ArrayList<>();

    @OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CrewMember> crewMember = new ArrayList<>();


    @CreatedDate
    private LocalDateTime createDate;
}
