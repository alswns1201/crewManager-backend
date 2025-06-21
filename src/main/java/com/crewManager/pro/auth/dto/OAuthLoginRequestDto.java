package com.crewManager.pro.auth.dto;


import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.auth.SocialType;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
public class OAuthLoginRequestDto {

    @JsonIgnore
    private SocialType socialType;

    private String authorizationCode;

    private String name;

    private String phoneNumber;

    private CrewMemberRole crewMemberRole;

    private String crewName;

}
