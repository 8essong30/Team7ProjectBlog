package com.sparta.blog.refresh;


import com.sparta.blog.refresh.jwt.TokenRequestDto;
import com.sparta.blog.refresh.jwt.TokenResponseDto;
import com.sparta.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Api(tags = {"5. Refresh Token RestAPI"})
public class RefreshController {
    private final UserService userService;
    @PostMapping("/reissue")
    @ApiOperation(value = "Reissue refresh token", notes = "Reissue refresh token Page")
    public TokenResponseDto reissue(@RequestBody TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        return userService.reissue(tokenRequestDto,response);
    }
}
