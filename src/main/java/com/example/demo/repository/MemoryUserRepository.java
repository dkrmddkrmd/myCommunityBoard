package com.example.demo.repository;

import com.example.demo.domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// @Repository // 지금은 SpringConfig로 관리하므로 어노테이션 제거
public class MemoryUserRepository implements UserRepository {

    private static Map<Long, User> userStore = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    @Override
    public User save(User user) {
        user.setId(++sequence);
        userStore.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userStore.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }
}