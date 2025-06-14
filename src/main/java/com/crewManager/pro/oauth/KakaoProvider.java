package com.crewManager.pro.oauth;


import com.crewManager.pro.oauth.dto.OAuthUserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProvider implements OAuthProvider {

    private final WebClient webClient = WebClient.create();

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("{spring.security.oauth2.client.registration.kakao.redirect-url}")
    private String KAKAO_REDIRECT_URI;

    @Value("{spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("spring.security.oauth2.client.provider.kakao.user-info-uri")
    private String KAKAO_USER_INFO_URI;




    @Override
    public SocialType getProviderType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        log.info("req kakao provider getAccess code : {}",authorizationCode);
        WebClient webClient = WebClient.create();
        HashMap<String,String> param = new HashMap<>();
        param.put("grant_type","authorization_code");
        param.put("client_id",KAKAO_CLIENT_ID);
        param.put("redirect_uri",KAKAO_REDIRECT_URI);
        param.put("code",authorizationCode);

        Map<String,Object> response =  webClient.post()
                .uri(KAKAO_TOKEN_URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=utf-8")
                .bodyValue(param)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        String accessToken = Objects.requireNonNull(response.get("access_token")).toString();
        if(accessToken == null){ throw new RuntimeException("kakao access-token error");}

        log.info("res kako getAccessToken  response {}",accessToken);
        return accessToken;

    }

    @Override
    public OAuthUserProfile getUserProfile(String accessToken) {
        log.info("req getUserProfile 요청 : {}",accessToken.substring(0,10));
        Map<String,Object> response = webClient.get()
                .uri(KAKAO_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+accessToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=utf-8")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        log.info("res kakao user resposne {}",response);
        Map<String,Object> kakaoAccount = (Map<String, Object>) Objects.requireNonNull(response.get("kakao_account"));
        Map<String,Object> profileInfo = (Map<String, Object>) kakaoAccount.get("profile");

        if(kakaoAccount.get("email") == null){
            throw new RuntimeException("kakao information email is error");
        }

        OAuthUserProfile userProfile = new OAuthUserProfile();
        userProfile.setProviderId(String.valueOf(response.get("id")));
        userProfile.setEmail((String) kakaoAccount.get("email"));
        userProfile.setNickname((String) profileInfo.get("nickname"));

        return userProfile;
    }



}
