package com.example.demo.service;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Long createPost(Long userId, Post post){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));

        post.setUser(user);

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional(readOnly = true)
    public List<Post> findPosts(){
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Post> findPost(Long postId){
        return postRepository.findById(postId);
    }

    public void updatePost(Long userId, Long postId, String newTitle, String newContent){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글 입니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("게시글을 수정할 권한이 없습니다.");
        }

        post.setTitle(newTitle);
        post.setContent(newContent);
    }

    public void deletePost(Long userId, Long postId){
        // 1. 삭제할 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));

        // 2. 권한 확인: 게시글 작성자와 삭제 요청자가 동일한지 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalStateException("게시글을 삭제할 권한이 없습니다.");
        }

        // 3. 게시글 삭제
        postRepository.deleteById(postId);
    }
}
