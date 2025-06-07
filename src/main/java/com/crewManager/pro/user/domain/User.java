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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewMember> crewMembers = new ArrayList<>();

//    // crewMember와는 다른연관관계로 유저는 여러 크루장 가능.
    @OneToMany(mappedBy = "crewLeader",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Crew> crewLeaders;

    //  Spring Security의 PasswordEncoder 사용 예정.
    private String password;

    private String name;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
