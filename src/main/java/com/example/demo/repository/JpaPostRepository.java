package com.example.demo.repository;

import com.example.demo.domain.Post;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaPostRepository implements PostRepository{
    private final EntityManager entityManager;

    public JpaPostRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Post save(Post post) {
        entityManager.persist(post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = entityManager.find(Post.class, id);
        return Optional.ofNullable(post);
    }

    @Override
    public List<Post> findAll() {
        return entityManager.createQuery("select p from Post p", Post.class).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        // 먼저 삭제할 엔티티를 조회한 후, remove 메서드를 호출합니다.
        findById(id).ifPresent(entityManager::remove);
    }
}
