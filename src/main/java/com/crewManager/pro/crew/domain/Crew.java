package com.crewManager.pro.crew.domain;

import com.crewManager.pro.user.domain.User;
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

    private CrewStatus status = CrewStatus.PENDING_APPROVAL;

    @OneToMany(mappedBy = "crew" ,cascade = CascadeType.ALL ,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CrewMember> crewMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewLeader_id", nullable = false)
    private User crewLeader;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;




}
