package com.example.demo.config; // 또는 com.example.demo.jwt

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime;

    // application.properties에서 시크릿 키와 만료 시간을 주입받음
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration_time}") long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    // 1. JWT 토큰 생성
    public String createToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .subject(username) // 토큰의 주체 (사용자 이름)
                .issuedAt(now) // 발급 시간
                .expiration(expiryDate) // 만료 시간
                .signWith(secretKey) // 서명에 사용할 키
                .compact();
    }

    // 2. JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // 토큰이 유효하지 않은 경우 (만료, 서명 불일치 등)
            return false;
        }
    }

    // 3. JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}