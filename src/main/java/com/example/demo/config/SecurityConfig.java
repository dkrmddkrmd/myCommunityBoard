package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API 서버는 csrf 보안이 필요 없으므로 비활성화
                .csrf(csrf -> csrf.disable())

                // REST API는 세션을 사용하지 않으므로 STATELESS로 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 요청 경로별 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        // 회원가입, 로그인은 토큰이 없어도 누구나 접근 가능해야 함
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll()
                        // 그 외의 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}