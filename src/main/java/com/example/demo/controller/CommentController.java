package com.example.demo.controller;

import com.example.demo.domain.Comment;
import com.example.demo.domain.User;
import com.example.demo.dto.CommentCreateRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "댓글 API", description = "댓글 관련 API")
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 작성", description = "특정 게시글에 새로운 댓글을 작성합니다.")
    @ApiResponse(responseCode = "201", description = "댓글 작성 성공")
    @PostMapping
    public ResponseEntity<Long> createComment(
            @Parameter(description = "댓글을 작성할 게시글 ID") @PathVariable Long postId,
            @RequestBody CommentCreateRequestDto requestDto,
            @AuthenticationPrincipal User user) {

        Long userId = user.getId();
        Long createdCommentId = commentService.createComment(userId, postId, requestDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentId);
    }

    @Operation(summary = "특정 게시글의 댓글 목록 조회", description = "특정 게시글에 달린 모든 댓글 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @Parameter(description = "조회할 게시글 ID") @PathVariable Long postId) {

        List<Comment> comments = commentService.findCommentsByPost(postId);
        List<CommentResponseDto> responseDtos = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}