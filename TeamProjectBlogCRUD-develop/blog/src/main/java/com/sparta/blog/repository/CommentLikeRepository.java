package com.sparta.blog.repository;

import com.sparta.blog.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    List<CommentLike> findByUsernameAndCommentId(String username, Long commentId);
    List<CommentLike> deleteByUsername(String username);
}
