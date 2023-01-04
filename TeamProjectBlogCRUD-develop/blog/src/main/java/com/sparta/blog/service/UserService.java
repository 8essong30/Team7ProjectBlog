package com.sparta.blog.service;

import com.sparta.blog.dto.request.SigninRequestDto;
import com.sparta.blog.dto.request.SignupRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.refresh.RefreshToken;
import com.sparta.blog.refresh.RefreshTokenJpaRepo;
import com.sparta.blog.refresh.jwt.TokenRequestDto;
import com.sparta.blog.refresh.jwt.TokenResponseDto;
import com.sparta.blog.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final RefreshTokenJpaRepo refreshTokenJpaRepo;


    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        //회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 role 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀렸습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);
        userRepository.save(user);
    }

    /**
     * Writer by Park
     * @param signinRequestDto
     * @param response
     * @return AccessToken, Refresh Token
     */
    @Transactional(readOnly = true)
    public TokenResponseDto signin(SigninRequestDto signinRequestDto, HttpServletResponse response) {
        String username = signinRequestDto.getUsername();
        String password = signinRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("미등록 사용자입니다.")
        );

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw  new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtUtil.createToken(user.getUsername(), user.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        String refreshToken1 = jwtUtil.refreshToken(username);
        //Making the refresh token object
        RefreshToken refreshToken = RefreshToken.builder().keys(user.getId()).token(refreshToken1).build();
        refreshTokenJpaRepo.save(refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }


    /**
     * Writer by Park
     * @param tokenRequestDto
     * @param response
     * @return Reissue AccessToken, RefreshToken
     */
    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        String refreshTokenResolved = jwtUtil.resolveTokenForRefreshToken(tokenRequestDto.getRefreshToken());
        // Expired RefreshToken -> Error
        if (!jwtUtil.validateToken(refreshTokenResolved)) {
            throw new IllegalStateException("expired token");
        }
        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = jwtUtil.resolveTokenForRefreshToken(tokenRequestDto.getAccessToken());

        //        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtUtil.getAuthentication(accessToken);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        Optional<User> user = userRepository.findByUsername(authentication.getName());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("no user in authentication");
        }
        RefreshToken refreshToken = refreshTokenJpaRepo.findByKeys(user.get().getId())
                .orElseThrow(IllegalArgumentException::new);

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new IllegalArgumentException("Refresh token is not matched");

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        String newCreatedToken = jwtUtil.createToken(user.get().getUsername(), user.get().getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newCreatedToken);
        String refreshToken1 = jwtUtil.refreshToken(user.get().getUsername());
        //Making the refresh token object
        RefreshToken refreshToken2 = RefreshToken.builder().keys(user.get().getId()).token(refreshToken1).build();
        RefreshToken updateRefreshToken = refreshToken.updateToken(refreshToken2);
        refreshTokenJpaRepo.save(updateRefreshToken);
        return new TokenResponseDto(newCreatedToken, updateRefreshToken);
    }


    @Transactional
    public boolean deleteUser(Long id,  User user) {
        //포스트 존재 여부 확인
        User users = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Invalid User"));
        if (users.isWriter(user.getUsername()) || UserRoleEnum.ADMIN == user.getRole()) {
            userRepository.deleteById(id);
            return true;
        } else {
            throw  new IllegalArgumentException("Invalid Writer");
        }
    }

}
