package com.example.demo.repository;

import com.example.demo.domain.Comment;
import com.example.demo.domain.Post;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class JpaCommentRepository implements CommentRepository{
    private final EntityManager entityManager;

    public JpaCommentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Comment save(Comment comment) {
        entityManager.persist(comment);

        return comment;
    }

    @Override
    public List<Comment> findByPostId(Long postId) {
        return entityManager.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }
}
