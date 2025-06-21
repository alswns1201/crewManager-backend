package com.crewManager.pro.auth;


import com.crewManager.pro.auth.dto.OAuthUserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
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

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_KEY;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;




    @Override
    public SocialType getProviderType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getAccessToken(String authorizationCode) {
        log.info("req kakao provider getAccess code : {}",authorizationCode);
        WebClient webClient = WebClient.create();
//        HashMap<String,String> param = new HashMap<>();
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("grant_type","authorization_code");
        param.add("client_id",KAKAO_CLIENT_ID);
        param.add("redirect_uri",KAKAO_REDIRECT_URI);
        param.add("client_secret", KAKAO_CLIENT_KEY); // <<< 가장 중요한 변경점!
        param.add("code",authorizationCode);

        Map<String,Object> response =  webClient.post()
                .uri(KAKAO_TOKEN_URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=utf-8")
//                .bodyValue(param)
                .body(BodyInserters.fromFormData(param)) // Map 대신 fromFormData 사용
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
