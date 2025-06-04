package com.crewManager.pro.crew.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@Embeddable @IdClass(CrewMemberId.class) 를 엔터티에 추가하면 @Embeddable 적용 x
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // equals()와 hashCode() 메서드 자동 생성 (복합 키 비교에 필수)\\
class CrewMemberId implements Serializable {
    private String user; // User 엔티티의 PK 타입과 일치
    private String crew; // Crew 엔티티의 PK 타입과 일치
}
