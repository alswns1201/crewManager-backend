package com.crewManager.pro.auth;

import com.crewManager.pro.auth.dto.OAuthUserProfile;

public interface OAuthProvider {
    SocialType getProviderType();
    String getAccessToken(String authorizationCode);
    OAuthUserProfile getUserProfile(String accessToken);

}
