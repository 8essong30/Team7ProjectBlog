package com.sparta.blog.controller;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.PostRepository;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Api(tags = {"4. Comment RestAPI"})
public class CommentController {

    private final JwtUtil jwtUtil;
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    @ApiOperation(value = "Create Comment", notes = "Create Comment Page")

    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequest, userDetails.getUser());
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value = "Update Comment", notes = "Update Comment Page")
    public CommentResponseDto updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(postId, commentId, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    @ApiOperation(value = "Delete Comment", notes = "Delete Comment Page")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(postId, commentId, userDetails.getUser());
    }

    @PostMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value = "Like or Cancel Like Comment", notes = "Like or Cancel Like Comment Page")
    public ResponseEntity<String> likeOrDislikeComment(@PathVariable Long postId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        return commentService.likeOrDislikeComment(commentId, username);
    }

}
