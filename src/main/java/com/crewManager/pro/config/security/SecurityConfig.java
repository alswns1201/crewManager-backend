package com.crewManager.pro.config.security;


import com.crewManager.pro.user.AppRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // [중요] 프론트엔드 주소를 정확히 적어줍니다.
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return  source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt는 강력한 해시 함수 중 하나입니다.
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf->csrf.disable())
                .formLogin(form->form.disable())
                .httpBasic(httpBasic->httpBasic.disable())
                //token으로 관리하기 위해 세션 관리 안함으로 설정
                .sessionManagement(session->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                // 토큰 인증 규칙
                .authorizeHttpRequests(authorize-> {
                    authorize.requestMatchers("/admin/**").hasRole(String.valueOf(AppRole.ADMIN));
                    // 로그인/회원가입은 인증 없이 허용 나머진 인증 적용.
                    authorize.requestMatchers("/api/auth/**").permitAll()
                            .anyRequest().authenticated();
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 프론트와 연결하기 위한 cors 정책 적용.
                // 기본 UsernamePassword 적용 전 커스텀 filter 먼저 확인.
                //UsernamePasswordAuthenticationFilter는 전통적인 폼 로그인(ID/PW)을 처리하는 Spring Security의 기본 필터
                // form 로그인을 사용하지 않지만 그 filter 앞에 커스텀 filter 를 적용하겠다는 설정.
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider) , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
