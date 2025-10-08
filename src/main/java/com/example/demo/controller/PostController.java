package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.PostForm;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 1. 게시글 작성
     */
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostForm form,
                                           @AuthenticationPrincipal User user) { // <-- 파라미터 추가

        // 임시 ID 대신, 실제 로그인한 사용자의 User 객체에서 ID를 가져옴
        Long userId = user.getId();

        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());

        Long createdPostId = postService.createPost(userId, post);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostId);
    }

    /**
     * 2. 전체 게시글 목록 조회
     */
    @GetMapping // "/api/posts"에 대한 GET 요청
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<Post> posts = postService.findPosts();

        // Post 엔티티 리스트를 PostResponseDto 리스트로 변환
        List<PostResponseDto> responseDtos = posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    /**
     * 3. 특정 게시글 상세 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        Optional<Post> postOptional = postService.findPost(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return ResponseEntity.ok(new PostResponseDto(post));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 4. 게시글 수정
     */
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostForm form, @AuthenticationPrincipal User user) {
        Long userId = user.getId();

        postService.updatePost(userId, postId, form.getTitle(), form.getContent());

        return ResponseEntity.ok("게시글 수정 성공");
    }

    /**
     * 5. 게시글 삭제
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
        Long userId = user.getId();

        postService.deletePost(userId, postId);

        return ResponseEntity.ok("게시글 삭제 성공");
    }
}