package com.example.demo.repository;

import com.example.demo.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    Optional<Post> findById(Long id);
    List<Post> findAll();
    void deleteById(Long id);
}
