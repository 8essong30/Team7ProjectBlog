package com.sparta.blog.dto.response;

import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    private List<CommentResponseDto> comments;


    //좋아요는??


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        this.comments = commentResponseDto.recursionDTO(post,0,0);
    }
}
