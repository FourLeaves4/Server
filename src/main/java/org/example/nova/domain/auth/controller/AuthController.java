package org.example.nova.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.dto.response.LoginResponseDto;
import org.example.nova.domain.auth.dto.response.LogoutResponseDto;
import org.example.nova.domain.auth.entity.User;
import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.example.nova.domain.auth.service.*;
import org.example.nova.global.security.jwt.dto.ReissueTokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ReissueService reissueService;
    private final OAuth2Service oAuth2Service;
    private final LogoutService logoutService;
    private final HttpServletResponse httpServletResponse;

    @GetMapping("/login")
    public ResponseEntity<String> loginRedirect() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/oauth2/authorization/google")
                .build();
    }

    /*
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
        User user = userService.findUserByEmail(loginRequest.getEmail());
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600);
        httpServletResponse.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(604800);
        httpServletResponse.addCookie(refreshTokenCookie);

        LoginResponseDto response = new LoginResponseDto(
                user.getUserId(),
                accessToken,
                refreshToken
        );

        return ResponseEntity.ok().headers(headers).body(response);
    }

     */

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> googleOAuthCallback(@RequestParam String code) {
        // Step 1: Google에서 Access Token 요청
        String googleToken = oAuth2Service.getGoogleAccessToken(code);

        // Step 2: Google에서 사용자 정보 요청
        OAuth2UserInfo userInfo = oAuth2Service.getGoogleUserInfo(googleToken);

        // Step 3: 사용자 저장 또는 조회
        User user = userService.findOrCreateUser(userInfo, "google");

        // Step 4: JWT 토큰 발급
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        // Step 5: 토큰 반환
        return ResponseEntity.ok(new LoginResponseDto(user.getUserId(), accessToken, refreshToken));
    }


    @GetMapping
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String token) {
        String jwt = jwtService.extractToken(token);
        String email = jwtService.getEmailFromToken(jwt);
        User user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestHeader("Authorization") String token) {
        LogoutResponseDto response = logoutService.logout(token);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reissue")
    public ResponseEntity<ReissueTokenResponseDto> reissue(@RequestHeader("refreshToken") String refreshToken) {
        return ResponseEntity.ok(reissueService.reissue(refreshToken));
    }

}

