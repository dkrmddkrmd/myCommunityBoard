package com.example.demo.repository;

import com.example.demo.domain.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaUserRepository implements UserRepository{
    private EntityManager entityManager;

    public JpaUserRepository(EntityManager em) {
        this.entityManager = em;
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> result = entityManager.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        // 리스트가 비어있지 않으면 첫 번째 요소를 반환, 비어있으면 Optional.empty() 반환
        return result.stream().findFirst();
    }
}
