package com.example.demo.service;

import com.example.demo.config.JwtUtil;
import com.example.demo.domain.Comment;
import com.example.demo.repository.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){ // <- 인터페이스에 의존
        return new UserService(userRepository, passwordEncoder, jwtUtil);
    }

    @Bean
    public PostService postService(PostRepository postRepository, UserRepository userRepository){
        return new PostService(postRepository, userRepository);
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository,PostRepository postRepository, UserRepository userRepository){
        return new CommentService(commentRepository, postRepository, userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
