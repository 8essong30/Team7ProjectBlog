package com.sparta.blog.controller;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.PostRepository;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final JwtUtil jwtUtil;
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public CommentResponseDto createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequest, HttpServletRequest request) {
        //Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);

        // 토큰 유효성 검증, 토큰에서 사용자 정보 가져오기
        //시큐리티 적용하실때 username 넘기는걸 userDetails.getUser() 넘기는 방식으로 바꿔주세요
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return commentService.createComment(postId, commentRequest, authenticatedUser.getUsername());
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            return commentService.updateComment(postId, commentId, requestDto, authenticatedUser.getUsername());
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }


    @DeleteMapping("/{blogId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long blogId, @PathVariable Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            commentService.deleteComment(blogId, commentId, authenticatedUser.getUsername());
            return new ResponseEntity<>("댓글 삭제 완료", HttpStatus.OK);
        }else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

    }

}
