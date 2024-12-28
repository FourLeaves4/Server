package org.example.nova.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.nova.domain.auth.dto.request.LoginRequestDto;
import org.example.nova.domain.auth.dto.response.LoginResponseDto;
import org.example.nova.domain.auth.entity.User;
import org.example.nova.domain.auth.service.JwtService;
import org.example.nova.domain.auth.service.LogoutService;
import org.example.nova.domain.auth.service.ReissueService;
import org.example.nova.domain.auth.service.UserService;
import org.example.nova.global.security.jwt.dto.ReissueTokenResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final UserService userService;
    private final ReissueService reissueService;
    private final LogoutService logoutService;
    private final HttpServletResponse httpServletResponse;

    @GetMapping("/login")
    public ResponseEntity<String> loginRedirect() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/oauth2/authorization/google")
                .build();
    }

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

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<String> googleOAuthCallback(@RequestParam String code) {
        return ResponseEntity.ok("OAuth2 callback received");
    }

    @GetMapping
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String token) {
        String jwt = jwtService.extractToken(token);
        String email = jwtService.getEmailFromToken(jwt);
        User user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        logoutService.logout(token);
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PatchMapping("/reissue")
    public ResponseEntity<ReissueTokenResponseDto> reissue(@RequestHeader("refreshToken") String refreshToken) {
        return ResponseEntity.ok(reissueService.reissue(refreshToken));
    }

}

