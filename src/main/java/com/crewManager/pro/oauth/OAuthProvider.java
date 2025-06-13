package com.crewManager.pro.oauth;

import com.crewManager.pro.oauth.dto.OAuthUserProfile;
import com.crewManager.pro.user.domain.SocialType;

public interface OAuthProvider {
    SocialType getProviderType();
    String getAccessToken(String authorizationCode);
    OAuthUserProfile getUserProfile(String accessToken);

}
