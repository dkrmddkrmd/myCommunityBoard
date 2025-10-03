package com.example.demo.service;


import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public class UserService{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long join(User user) {
        // 1. 중복 회원 검증 (비즈니스 로직)
        validateDuplicateUser(user);

        // 2. 검증 통과 후 저장
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(u -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public Optional<User> findUser(Long userId) {
        // userRepository.findById가 이미 Optional을 반환하므로 그대로 반환
        return userRepository.findById(userId);
    }
}
