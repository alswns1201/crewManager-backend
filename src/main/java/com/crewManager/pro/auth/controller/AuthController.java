package com.crewManager.pro.auth.controller;


import com.crewManager.pro.auth.OAuthProvider;
import com.crewManager.pro.auth.SocialType;
import com.crewManager.pro.auth.dto.OAuthLoginRequestDto;
import com.crewManager.pro.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/{provider}")
    public ResponseEntity<String> socialLogin(@PathVariable String provider, @RequestBody OAuthLoginRequestDto requestDto){
        log.info("socialLogin provider {} ,dto {}",provider,requestDto.toString());
        SocialType socialType = SocialType.valueOf(provider.toUpperCase());
        requestDto.setSocialType(socialType);

        String jwtToken = authService.login(requestDto);
        log.info("socialLogin success {}",jwtToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+jwtToken);
        return new ResponseEntity<>(jwtToken,headers, HttpStatus.OK);
    }


}
