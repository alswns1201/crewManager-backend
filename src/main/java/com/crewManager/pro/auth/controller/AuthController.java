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

        // ✅ JWT를 HttpOnly 쿠키로 설정
        ResponseCookie cookie = ResponseCookie.from("accessToken", jwtToken)
                .httpOnly(true)
                .secure(false) // 배포시 true 이여야 https 가능
                .path("/")
//                .sameSite("None") // 클라이언트가 서버와 다른 도메인인 경우 https에서만 사용 가능.
                .maxAge(60 * 60 * 24) // 1일
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("로그인 성공");
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

        // Set-Cookie 헤더에 삭제할 쿠키를 담아 응답합니다.
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("로그아웃 되었습니다.");
    }

}
