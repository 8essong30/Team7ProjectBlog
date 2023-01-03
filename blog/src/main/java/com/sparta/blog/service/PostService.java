package com.sparta.blog.service;

import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.Post;
import com.sparta.blog.entity.User;
import com.sparta.blog.repository.PostRepository;

import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

        System.out.println("PostService.createPost");
        System.out.println("user.getUsername() = " + user.getUsername());

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user));

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllBlogs() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();
        List<PostResponseDto> postResponseDto = new ArrayList<>();
        for (Post post : posts){
            postResponseDto.add(new PostResponseDto(post));
        }
        return postResponseDto;
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPosts(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, String requestedUsername) {

        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );
        Post post = postRepository.findByIdAndUserId(postId, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public ResponseEntity<String> deletePost(Long id, String requestedUsername) {

        User user = userRepository.findByUsername(requestedUsername).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        Post post = postRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        postRepository.deleteById(id);
        return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
    }

}