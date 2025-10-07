package com.example.demo.service;


import com.example.demo.config.JwtUtil;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입
    private final JwtUtil jwtUtil; // JwtUtil 주입

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Long join(User user) {
        validateDuplicateUser(user); // 중복 회원 검증

        // 비밀번호를 암호화해서 저장
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    public String login(String username, String password) {
        // 1. 사용자 이름으로 User 조회
        User user = userRepository.findByUsername(username)
                .orElse(null);

        // 2. 사용자가 존재하고, 비밀번호가 일치하는지 확인
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 3. 로그인 성공 시 JWT 토큰 생성하여 반환
            return jwtUtil.createToken(user.getUsername());
        }

        // 4. 로그인 실패 시 null 반환
        return null;
    }

    public Optional<User> findUser(Long userId) {
        // userRepository.findById가 이미 Optional을 반환하므로 그대로 반환
        return userRepository.findById(userId);
    }
}
