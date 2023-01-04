package com.sparta.blog.controller;

import com.sparta.blog.dto.request.SigninRequestDto;
import com.sparta.blog.dto.request.SignupRequestDto;
import com.sparta.blog.refresh.jwt.TokenResponseDto;
import com.sparta.blog.security.UserDetailsImpl;
import com.sparta.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Api(tags = {"1. Get User API"})
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ApiOperation(value = "Get SignUp", notes = "Get SignUp Page")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return "success";
    }
    @ResponseBody
    @PostMapping("/signin")
    @ApiOperation(value = "Post signin", notes = "Post signIn page")
    public TokenResponseDto login(@RequestBody SigninRequestDto signinRequestDto) {
        return userService.signin(signinRequestDto);
    }


    /**
     * Delete user
     * Writer by Park
     */
    @PreAuthorize("isAuthenticated() and (( #userDetails.username == principal.username ) or hasRole('ADMIN'))")
    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Delete User", notes = "Delete User page")
    public String deleteUser(@PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userService.deleteUser(id, userDetails.getUser())) {
            return "Success delete user";
        } else {
            throw new IllegalArgumentException("Failed delete user");
        }
    }
}
