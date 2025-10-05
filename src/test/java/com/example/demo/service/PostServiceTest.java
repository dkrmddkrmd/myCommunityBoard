package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.MemoryPostRepository;
import com.example.demo.repository.MemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {

    PostService postService;
    MemoryPostRepository postRepository;
    MemoryUserRepository userRepository;

    User user1; // 테스트용 사용자1
    User user2; // 테스트용 사용자2

    @BeforeEach
    void beforeEach() {
        postRepository = new MemoryPostRepository();
        userRepository = new MemoryUserRepository();
        postService = new PostService(postRepository, userRepository);

        // 테스트에 사용할 사용자 미리 저장
        user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);
    }

    @Test
    void 게시글_작성_성공() {
        // given
        Post post = new Post();
        post.setTitle("테스트 제목");

        // when
        Long savedPostId = postService.createPost(user1.getId(), post);

        // then
        Post foundPost = postRepository.findById(savedPostId).get();
        assertThat(foundPost.getUser().getId()).isEqualTo(user1.getId());
        assertThat(foundPost.getTitle()).isEqualTo("테스트 제목");
    }

    @Test
    void 다른_사용자가_게시글_수정시_예외_발생() {
        // given
        Post post = new Post();
        post.setTitle("user1의 글");
        Long savedPostId = postService.createPost(user1.getId(), post);

        // when & then
        // user2가 user1의 글을 수정하려고 할 때 예외가 발생하는지 검증
        assertThrows(IllegalStateException.class, () -> {
            postService.updatePost(user2.getId(), savedPostId, "바뀐 제목", "바뀐 내용");
        });
    }

    // ... 다른 테스트 케이스들 구현 ...
}