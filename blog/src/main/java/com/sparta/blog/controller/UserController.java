package com.sparta.blog.controller;

import com.sparta.blog.dto.request.SigninRequestDto;
import com.sparta.blog.dto.request.SignupRequestDto;
import com.sparta.blog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return "success";
    }

    @PostMapping("/signin")
    public String login(@RequestBody SigninRequestDto signinRequestDto, HttpServletResponse response) {
        userService.signin(signinRequestDto, response);
        return "success";
    }
}
