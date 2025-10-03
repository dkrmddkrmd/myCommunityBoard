package com.example.demo.repository;

import com.example.demo.domain.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findByPostId(Long postId);
}
