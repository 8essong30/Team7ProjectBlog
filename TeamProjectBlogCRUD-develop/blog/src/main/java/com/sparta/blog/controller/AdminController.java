package com.sparta.blog.controller;

import com.sparta.blog.dto.request.CommentRequestDto;
import com.sparta.blog.dto.request.PostRequestDto;
import com.sparta.blog.dto.response.AuthenticatedUser;
import com.sparta.blog.dto.response.CommentResponseDto;
import com.sparta.blog.dto.response.PostResponseDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;


    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/posts/{id}")
    @Operation(summary = "Update post by admin", description = "Update post by admin Page")
    public PostResponseDto updatePostByAdmin(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN))
                throw new IllegalArgumentException("권한이 없습니다.");
            return adminService.updatePostByAdmin(id, requestDto);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/posts/{postId}")
    @Operation(summary = "Delete post by admin", description = "Delete post by admin Page")
    public ResponseEntity<String> deletePostByAdmin(@PathVariable Long postId, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN))
                throw new IllegalArgumentException("권한이 없습니다.");
            return adminService.deletePostByAdmin(postId);
        } else {
            return new ResponseEntity<>("토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @PutMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Update comment by admin", description = "Update comment by admin Page")
    public CommentResponseDto updateCommentByAdmin(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN))
                throw new IllegalArgumentException("권한이 없습니다");
            return adminService.updateCommentByAdmin(postId, commentId, requestDto);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }
    }

    @Secured(UserRoleEnum.Authority.ADMIN)
    @DeleteMapping("/{postId}/comment/{commentId}")
    @Operation(summary = "Delete comment by admin", description = "Delete comment by admin Page")
    public ResponseEntity<String> deleteCommentByAdmin(@PathVariable Long postId, @PathVariable Long commentId, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token != null) {
            AuthenticatedUser authenticatedUser = jwtUtil.validateTokenAndGetInfo(token);
            if (!authenticatedUser.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
                throw new IllegalArgumentException("권한이 없습니다");
            }
            adminService.deleteCommentByAdmin(postId, commentId);
            return new ResponseEntity<>("댓글 삭제 완료", HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

    }

}
