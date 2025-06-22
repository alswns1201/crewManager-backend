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

    @JsonIgnore
    private String name;
    @JsonIgnore
    private String phoneNumber;
    @JsonIgnore
    private CrewMemberRole crewMemberRole;
    @JsonIgnore
    private String crewName;

}
