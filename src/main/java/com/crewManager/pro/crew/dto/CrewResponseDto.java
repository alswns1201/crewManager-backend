package com.crewManager.pro.crew.dto;


import com.crewManager.pro.crew.domain.Crew;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CrewResponseDto {

    private String id;

    private String name;

    // Crew 엔티티를 받아서 DTO를 생성하는 생성자
    public CrewResponseDto(Crew crew) {
        this.id = crew.getId();
        this.name = crew.getName();
    }

}
