package com.crewManager.pro.config;


import com.crewManager.pro.config.security.JwtTokenProvider;
import com.crewManager.pro.user.AppRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityTokenTest {


    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(){
        String testSecretKey = "3JfVb1FbPpOZgqugaaThvp1Kc4OyRP9nDXUjavPO1E=";
        jwtTokenProvider = new JwtTokenProvider(testSecretKey);
    }

    @Test
    public void key만들기(){
            SecureRandom random = new SecureRandom();
            byte[] keyBytes = new byte[32]; // 256 bits
            random.nextBytes(keyBytes);
            String secretKey = Base64.getEncoder().encodeToString(keyBytes);
            System.out.println("Generated Secret Key: " + secretKey);

    }

    @Test
    @DisplayName("Authentication 객체로 JWT 토큰을 성공적으로 생성 확인")
    public void  generateToken(){
        // given
        String userId ="user-id-1234";
        AppRole appRole = AppRole.GENERAL;
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userId,null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLU_"+AppRole.GENERAL))
                );
        // when
        String token = jwtTokenProvider.generateToken(authentication);
        String untoken = "unsafe12312312312312313123";
        boolean isValid = jwtTokenProvider.validateToken(token);
        boolean isUnValid = jwtTokenProvider.validateToken(untoken);

        //then
        assertThat(token).isNotNull();
        assertThat(isValid).isTrue();
        assertThat(isUnValid).isFalse();

        System.out.println("token : "+token);
    }

    @Test
    @DisplayName("유효한 토근값 으로 복원")
    public void getAuthentication(){
        // given
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyLWlkLTEyMzQiLCJhdXRoIjoiUk9MVV9HRU5FUkFMIiwiZXhwIjoxNzQ5ODk3MzYzfQ.YOJsBeIMe458CBYT4-PqiDmxIYAORR3HOePvVOtPLWY";

        //when
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        assertThat("user-id-1234").isEqualTo(authentication.getName());
        assertThat(authentication.getAuthorities()).extracting(GrantedAuthority::getAuthority);


    }


}
