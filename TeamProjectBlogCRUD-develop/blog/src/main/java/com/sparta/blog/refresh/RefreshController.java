package com.sparta.blog.refresh;


import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.refresh.jwt.TokenRequestDto;
import com.sparta.blog.refresh.jwt.TokenResponseDto;
import com.sparta.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "Bearer Authentication")
public class RefreshController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/reissue")
    @Operation(summary = "Reissue refresh token", description = "Reissue refresh token Page")
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

    //끝났다는말이expire
    //다시로그인할필요가없다.
    //리프레쉬토큰이 만료면 로그인페이지
    //리프레쉬토큰이 리이슈가되니까
    //access토큰이 계속재발하게된다.
    //

}