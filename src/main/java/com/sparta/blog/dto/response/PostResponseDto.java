package com.sparta.blog.dto.response;

import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.PostLike;
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

    private int postLikes;

    private List<PostLikeResponseDto> likedUsers;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        List<CommentResponseDto> list = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            list.add(new CommentResponseDto(comment));
        }
        this.comments = list;

        this.postLikes = post.getPostLikes().size();

        List<PostLikeResponseDto> likedUserList = new ArrayList<>();
        for (PostLike postLike : post.getPostLikes()) {
            likedUserList.add(new PostLikeResponseDto(postLike));
        }
        this.likedUsers = likedUserList;
    }
}
