package com.crewManager.pro.oauth.service;


import com.crewManager.pro.config.security.JwtTokenProvider;
import com.crewManager.pro.oauth.OAuthProviderFactory;
import com.crewManager.pro.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final OAuthProviderFactory providerFactory;


    @Transactional
    public void login(){

    }


}
