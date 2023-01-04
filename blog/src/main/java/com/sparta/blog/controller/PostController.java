package com.sparta.blog.controller;

import com.sparta.blog.dto.request.PageDTO;
import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getAllPost() {
        return postService.getAllPost();
    }

    @GetMapping("/posts/page")
    public List<PostResponseDto> getPagingPost(@RequestBody PageDTO pageDTO){
        return postService.getPagingPost(pageDTO);
    }

    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPosts(id);
    }

    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }

    @PostMapping("/posts/{id}")
    public ResponseEntity<String> likeOrDislikePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likeOrDislikePost(id, userDetails.getUser());
    }
}
