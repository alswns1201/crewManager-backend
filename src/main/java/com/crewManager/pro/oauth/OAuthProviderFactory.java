package com.crewManager.pro.oauth;


import com.crewManager.pro.user.domain.SocialType;
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


    public OAuthProvider getProvider(SocialType socialType){
        return providers.get(socialType);
    }


}
