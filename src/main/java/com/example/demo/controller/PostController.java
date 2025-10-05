package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.dto.PostForm;
import com.example.demo.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts") // Post 관련 API는 모두 /api/posts로 시작
public class PostController {

    private final PostService postService;

    // PostService를 주입받음
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 1. 게시글 작성
     * POST /api/posts
     */
    @PostMapping("post")
    public ResponseEntity<String> createPost(@RequestBody PostForm form) {
        // TODO: userId는 나중에 로그인 기능 구현 후 인증 정보에서 가져옵니다.

        // TODO: PostForm DTO를 Post 엔티티로 변환하여 postService.createPost()를 호출하는 로직 구현


        return ResponseEntity.ok("게시글 작성 성공");
    }

    /**
     * 2. 전체 게시글 목록 조회
     * GET /api/posts
     */
    @GetMapping
    public ResponseEntity<?> getPosts() {
        // TODO: postService.findPosts()를 호출하여 모든 게시글을 조회하는 로직 구현\

        return null;
    }

    /**
     * 3. 특정 게시글 상세 조회
     * GET /api/posts/{postId}
     */
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        // TODO: postService.findPost(postId)를 호출하여 특정 게시글을 조회하는 로직 구현
        return null;
    }

    /**
     * 4. 게시글 수정
     * PUT /api/posts/{postId}
     */
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostForm form) {
        // TODO: userId는 나중에 인증 정보에서 가져옵니다.
        // TODO: postService.updatePost()를 호출하여 게시글을 수정하는 로직 구현
        return ResponseEntity.ok("게시글 수정 성공");
    }

    /**
     * 5. 게시글 삭제
     * DELETE /api/posts/{postId}
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        // TODO: userId는 나중에 인증 정보에서 가져옵니다.
        // TODO: postService.deletePost()를 호출하여 게시글을 삭제하는 로직 구현
        return ResponseEntity.ok("게시글 삭제 성공");
    }
}