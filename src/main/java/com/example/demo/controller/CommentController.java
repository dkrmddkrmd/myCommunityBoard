package com.example.demo.controller;

import com.example.demo.domain.Comment;
import com.example.demo.dto.CommentCreateRequestDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}/comments") // '/api/posts/{postId}/comments' 주소에 대한 요청을 처리
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 1. 댓글 작성
     * POST /api/posts/{postId}/comments
     */
    @PostMapping
    public ResponseEntity<Long> createComment(@PathVariable Long postId,
                                              @RequestBody CommentCreateRequestDto requestDto) {
        // TODO: userId는 나중에 로그인 기능 구현 후 인증 정보에서 가져옵니다. 지금은 임시로 1L 사용.
        Long tempUserId = 1L;

        Long createdCommentId = commentService.createComment(tempUserId, postId, requestDto.getContent());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentId);
    }

    /**
     * 2. 특정 게시글의 댓글 목록 조회
     * GET /api/posts/{postId}/comments
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.findCommentsByPost(postId);

        List<CommentResponseDto> responseDtos = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }
}