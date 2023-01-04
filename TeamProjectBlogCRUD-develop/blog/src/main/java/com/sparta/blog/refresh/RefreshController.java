package com.sparta.blog.refresh;


import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.refresh.jwt.TokenRequestDto;
import com.sparta.blog.refresh.jwt.TokenResponseDto;
import com.sparta.blog.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Api(tags = {"5. Refresh Token RestAPI"})
public class RefreshController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    @ApiOperation(value = "Reissue refresh token", notes = "Reissue refresh token Page")
    public TokenResponseDto reissue(HttpServletRequest request, @RequestBody TokenRequestDto tokenRequestDto) {
        String resolvedAccessToken = jwtUtil.resolveAccessToken(tokenRequestDto.getAccessToken());
        //Access 토큰 username가져오기
        Authentication authenticationAccessToken = jwtUtil.getAuthentication(resolvedAccessToken);
        User accessUser = userService.findByUsername(authenticationAccessToken.getName());
        //Refrest 토큰 username가져오기
        String refreshToken = request.getHeader(JwtUtil.REFRESH_AUTHORIZATION_HEADER);
        String resolvedRefreshToken = jwtUtil.resolveRefreshToken(refreshToken);
        Authentication authenticationFreshToken = jwtUtil.getAuthentication(resolvedRefreshToken);
        User refreshUser = userService.findByUsername(authenticationFreshToken.getName());
        //두개 비교 후 맞으면 재발행
        if (accessUser == refreshUser) {
            return userService.reissue(refreshUser.getUsername(), refreshUser.getRole());
        }
        throw new IllegalStateException("Vaild Error");
    }
}