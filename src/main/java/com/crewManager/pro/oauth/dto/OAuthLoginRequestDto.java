package com.crewManager.pro.oauth.dto;


import com.crewManager.pro.crew.domain.CrewMemberRole;
import com.crewManager.pro.oauth.SocialType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthLoginRequestDto {

    private SocialType socialType;

    private String authorizationCode;

    private String name;

    private String phoneNumber;

    private CrewMemberRole crewMemberRole;

    private String crewName;

}
