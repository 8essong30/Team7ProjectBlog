package com.sparta.blog.repository;

import com.sparta.blog.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findByUsernameAndPostId(String username, Long postId);
    List<PostLike> deleteByUsername(String username);
}