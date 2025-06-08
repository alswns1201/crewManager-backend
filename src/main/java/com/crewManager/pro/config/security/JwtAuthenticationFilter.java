package com.crewManager.pro.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider; // JWT 생성 및 검증을 담당할 클래스

    // 이 필터의 핵심 로직이 담긴 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 2. 요청 헤더에서 JWT 토큰을 추출합니다.
        String token = resolveToken(request);

        // 3. 토큰이 유효한지 검증합니다.
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 4. 토큰이 유효하면, 토큰으로부터 사용자 정보를 받아옵니다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // 5. SecurityContextHolder에 인증 정보를 저장합니다.
            //    이 시점부터 해당 사용자는 "인증된 사용자"로 취급됩니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 6. 다음 필터로 제어를 넘깁니다.
        filterChain.doFilter(request, response);
    }

    // 요청 헤더에서 "Authorization" 필드 값을 가져와 "Bearer " 부분을 잘라내고 토큰만 반환합니다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // StringUtils.hasText()는 null, 빈 문자열, 공백만 있는 문자열인지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}