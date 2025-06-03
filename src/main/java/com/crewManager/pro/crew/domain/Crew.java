package com.crewManager.pro.crew.domain;


import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "crews")
public class Crew {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(nullable = false, unique = true) // 크루 이름은 유니크해야 함
    private String name;

    @Column(length = 500) // 설명은 길 수 있으므로 길이 제한
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private CrewStatus status = CrewStatus.UNAPPROVAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_user_id", nullable = false) // leader_user_id 라는 컬럼 생성
    //모임장은 User와  일대 다 , 하나의 유저가 여러 모임장 가능
    private User userLeader;

    @OneToMany(mappedBy = "crew" ,cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CrewMember> crewMember = new ArrayList<>();

    //멤버는 crewMember와 연결

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;




}
