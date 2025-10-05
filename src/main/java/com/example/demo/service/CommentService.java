package com.example.demo.service;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * 댓글 작성
     */
    public Long createComment(Long userId, Long postId, String content) {
        // 1. 사용자(User)와 게시글(Post) 엔티티를 DB에서 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원입니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));

        // 2. 새로운 Comment 객체 생성
        Comment newComment = new Comment();
        newComment.setContent(content);

        // 3. 연관관계 설정 (ID가 아닌, 조회한 엔티티 객체 자체를 설정)
        newComment.setUser(user);
        newComment.setPost(post);

        // 4. 댓글 저장
        commentRepository.save(newComment);

        // 5. 생성된 댓글의 ID 반환
        return newComment.getId();
    }

    /**
     * 특정 게시글의 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Comment> findCommentsByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시글입니다."));

        return commentRepository.findByPostId(postId);
    }
}