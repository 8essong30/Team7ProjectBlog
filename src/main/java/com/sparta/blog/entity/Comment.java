package com.sparta.blog.entity;

import com.sparta.blog.dto.request.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "post_id")
    private Post post;

    private String writer;

    public Comment(CommentRequestDto commentRequestDto, Post post, String writer) {
        this.contents = commentRequestDto.getContents();
        this.post = post;
        this.writer = writer;
    }

    public void updateComment(CommentRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }

    public boolean isCommentWriter(String inputUsername) {
        return this.writer.equals(inputUsername);
    }
}
