package com.crewManager.pro.oauth;

import com.crewManager.pro.oauth.dto.OAuthUserProfile;

public interface OAuthProvider {
    SocialType getProviderType();
    String getAccessToken(String authorizationCode);
    OAuthUserProfile getUserProfile(String accessToken);

}
