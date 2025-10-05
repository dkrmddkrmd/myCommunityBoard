package com.example.demo.repository;

import com.example.demo.domain.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryPostRepository implements PostRepository {

    private static Map<Long, Post> postStore = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    @Override
    public Post save(Post post) {
        post.setId(++sequence);
        postStore.put(post.getId(), post);
        return post;
    }

    @Override
    public Optional<Post> findById(Long id) {
        Post post = postStore.get(id);
        return Optional.ofNullable(post);
    }

    @Override
    public List<Post> findAll() {
        // Map의 모든 값(Post 객체들)을 가져와서 새로운 리스트로 만들어 반환합니다.
        return new ArrayList<>(postStore.values());
    }

    @Override
    public void deleteById(Long id) {
        // id를 키로 사용하여 Map에서 해당 Post 객체를 제거합니다.
        postStore.remove(id);
    }
}