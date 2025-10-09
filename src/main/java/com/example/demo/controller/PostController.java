package com.example.demo.controller;

import com.example.demo.domain.Post;
import com.example.demo.domain.User;
import com.example.demo.dto.PostForm;
import com.example.demo.dto.PostResponseDto;
import com.example.demo.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "게시글 API", description = "게시글 관련 API")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성하고 생성된 게시글의 ID를 반환합니다.")
    @ApiResponse(responseCode = "201", description = "게시글 작성 성공")
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostForm form,
                                           @AuthenticationPrincipal User user) {
        Long userId = user.getId();
        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        Long createdPostId = postService.createPost(userId, post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostId);
    }

    @Operation(summary = "전체 게시글 목록 조회", description = "모든 게시글의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<Post> posts = postService.findPosts();
        List<PostResponseDto> responseDtos = posts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "특정 게시글 상세 조회", description = "ID로 특정 게시글의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId
    ) {
        Optional<Post> postOptional = postService.findPost(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            return ResponseEntity.ok(new PostResponseDto(post));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글의 제목과 내용을 수정합니다. (작성자 본인만 가능)")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @ApiResponse(responseCode = "403", description = "수정 권한 없음")
    @PutMapping("/{postId}")
    public ResponseEntity<String> updatePost(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @RequestBody PostForm form,
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getId();
        postService.updatePost(userId, postId, form.getTitle(), form.getContent());
        return ResponseEntity.ok("게시글 수정 성공");
    }

    @Operation(summary = "게시글 삭제", description = "기존 게시글을 삭제합니다. (작성자 본인만 가능)")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "403", description = "삭제 권한 없음")
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getId();
        postService.deletePost(userId, postId);
        return ResponseEntity.ok("게시글 삭제 성공");
    }
}