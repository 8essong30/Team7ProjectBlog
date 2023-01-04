package com.sparta.blog.refresh.jwt;

import com.sparta.blog.refresh.RefreshToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(String accessToken,  RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken.getToken();
    }
}
