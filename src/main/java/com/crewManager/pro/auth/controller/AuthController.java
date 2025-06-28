package com.crewManager.pro.auth.controller;


import com.crewManager.pro.auth.SocialType;
import com.crewManager.pro.auth.dto.OAuthLoginRequestDto;
import com.crewManager.pro.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

    // [새로 추가] 로그아웃 메소드
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        log.info("로그아웃 요청 처리 시작");

        // 쿠키를 삭제하기 위해 만료 시간을 0으로 설정한 쿠키를 생성합니다.
        ResponseCookie deleteCookie = ResponseCookie.from("accessToken", "") // 쿠키 값을 비웁니다.
                .path("/")
                .httpOnly(true)
                .secure(false)// https 에서는 true , http 에서는 false로 해야함.
                .maxAge(0) // 만료 시간을 0으로 설정하여 즉시 삭제되도록 합니다.
                .build();

        log.info("로그아웃 쿠키 생성 완료. 클라이언트로 전송합니다.");

        // Set-Cookie 헤더에 삭제할 쿠키를 담아 응답합니다.
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("로그아웃 되었습니다.");
    }

}
