package com.sparta.blog.controller;

import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    public List<PostResponseDto> getAllBlogs() {
        return postService.getAllBlogs();
    }

    @GetMapping("/posts/{postId}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPosts(id);
    }

    @PutMapping("/posts/{postId}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUsername());
}

    @DeleteMapping("/posts{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return postService.deletePost(id, authenticatedUser.getUsername());
        } else {
            return new ResponseEntity<>("토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

}