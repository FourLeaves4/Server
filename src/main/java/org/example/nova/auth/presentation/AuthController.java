package org.example.nova.auth.presentation;

import jakarta.servlet.http.HttpSession;
import org.example.nova.auth.entity.User;
import org.example.nova.auth.service.UserService;
import org.example.nova.token.JwtTokenProvider;
import org.example.nova.token.LoginRequestDto;
import org.example.nova.token.RefreshTokenService;
import org.example.nova.token.TokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    // JWT 기반 로그인
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증 실패
        }

        // JWT 생성
        String accessToken = jwtTokenProvider.createToken(user.getName());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getName());

        // JSON으로 토큰 반환
        return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken));
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("로그아웃 완료 (클라이언트에서 토큰 삭제 필요)", HttpStatus.OK);
    }
}


/*
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider; // JWT 토큰 관리
    private final UserService userService; // 사용자 정보 관리 서비스

    // JWT를 이용한 로그인 사용자 정보 조회
    @GetMapping
    public ResponseEntity<LoginResponseDto> getLoginUser(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.substring(7); // "Bearer " 제거
            if (!jwtTokenProvider.validateToken(token)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            String username = jwtTokenProvider.getUsername(token);
            User loginUser = userService.findByUsername(username);
            if (loginUser == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            return ResponseEntity.ok(new LoginResponseDto(loginUser.getUserId()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginRedirect() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/oauth2/authorization/google")
                .build();
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String accessToken = jwtTokenProvider.createToken(user.getName());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getName());

        return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("로그아웃 완료 (클라이언트에서 토큰 삭제 필요)", HttpStatus.OK);
    }
}

 */

