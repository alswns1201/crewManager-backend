package com.crewManager.pro.crew.domain;


import com.crewManager.pro.user.domain.User;
import jakarta.persistence.*;

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



}
