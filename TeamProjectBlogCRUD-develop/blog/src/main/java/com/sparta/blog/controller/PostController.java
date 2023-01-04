package com.sparta.blog.controller;

import com.sparta.blog.dto.request.PageDTO;
import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"3. Post RestAPI"})
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    @PostMapping("/posts")
    @ApiOperation(value = "Create Post", notes = "Create post Page")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser().getUsername());
    }

    @GetMapping("/posts")
    @ApiOperation(value = "View all post list", notes = "View all post list Page")
    public List<PostResponseDto> getAllPost() {
        return postService.getAllPost();
    }

    @GetMapping("/posts/page")
    @ApiOperation(value = "Get paging post ", notes = "Get paging post Page")
    public List<PostResponseDto> getPagingPost(@RequestBody PageDTO pageDTO){
        return postService.getPagingPost(pageDTO);
    }

    @GetMapping("/posts/{id}")
    @ApiOperation(value = "View only one post ", notes = "View only one post Page")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPosts(id);
    }

    @PutMapping("/posts/{id}")
    @ApiOperation(value = "Update Post", notes = "Update post Page")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUser().getUsername());
    }

    @DeleteMapping("/posts/{id}")
    @ApiOperation(value = "Delete Post", notes = "Delete post Page")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser().getUsername());
    }

    @PostMapping("/posts/{id}")
    @ApiOperation(value = "Like or Cancel like post", notes = "Like or Cancel Like post Page")
    public ResponseEntity<String> likeOrDislikePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return postService.likeOrDislikePost(id, username);
    }
}
