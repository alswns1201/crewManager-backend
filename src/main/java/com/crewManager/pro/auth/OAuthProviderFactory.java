package com.crewManager.pro.auth;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OAuthProviderFactory {

    private final Map<SocialType,OAuthProvider> providers;


    public OAuthProviderFactory(List<OAuthProvider> providerList) {
        this.providers = providerList.stream().collect(Collectors.toUnmodifiableMap(OAuthProvider::getProviderType, Function.identity()));
    }

    /**
     * 생성자에서 구성된 providers에 대한 특정 key값을 return
     * @param socialType
     * @return
     */
    public OAuthProvider getProvider(SocialType socialType){
        return providers.get(socialType);
    }


}
