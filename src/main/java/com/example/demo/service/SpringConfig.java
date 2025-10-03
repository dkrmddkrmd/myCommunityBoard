package com.example.demo.service;

import com.example.demo.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final EntityManager entityManager;

    // 스프링이 EntityManager를 주입해줌
    @Autowired
    public SpringConfig(EntityManager em) {
        this.entityManager = em;
    }

    // UserRepository를 빈으로 등록
    @Bean
    public UserRepository userRepository() {
        // 우리가 직접 만든 JpaUserRepository를 생성해서 등록
        // 이때 EntityManager를 넘겨줌
        return new JpaUserRepository(entityManager);
    }

    @Bean
    public PostRepository postRepository(){
        return new JpaPostRepository(entityManager);
    }

    @Bean
    public CommentRepository commentRepository(){
        return new JpaCommentRepository(entityManager);
    }
}
