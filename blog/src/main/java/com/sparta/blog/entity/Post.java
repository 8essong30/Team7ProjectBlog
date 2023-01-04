package com.sparta.blog.entity;

import com.sparta.blog.dto.request.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany (mappedBy = "post", cascade = CascadeType.REMOVE)
    // 1.그룹순 2.깊이순 3.reforder순으로 정렬필요
    @OrderBy("ref asc , deps asc, refOrder asc")
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
