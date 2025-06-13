package com.crewManager.pro.oauth;


import com.crewManager.pro.oauth.dto.OAuthUserProfile;
import com.crewManager.pro.user.domain.SocialType;
import org.springframework.stereotype.Component;

@Component
public class KakaoProvider implements OAuthProvider {

    @Override
    public SocialType getProviderType() {
        return null;
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        return null;
    }

    @Override
    public OAuthUserProfile getUserProfile(String accessToken) {
        return null;
    }



}
