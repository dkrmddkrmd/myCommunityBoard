package repository;

import domain.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id); // PK로 찾는 메서드
    Optional<User> findByUsername(String username); // 로그인 시 사용할, username으로 찾는 메서드
}
