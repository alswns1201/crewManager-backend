package com.crewManager.pro.auth.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserProfile {
    private String email;
    private String nickname;
    private String providerId; // 플랫폼 고유 ID
}
