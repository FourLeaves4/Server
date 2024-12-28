package org.example.nova.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CookieController {

    @PostMapping("/set-cookie")
    public ResponseEntity<String> setCookie(HttpServletResponse response) {
        // 쿠키 생성
        Cookie cookie = new Cookie("accessToken", "your_jwt_token_here");

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);

        return ResponseEntity.ok("쿠키 설정 완료");
    }

    @GetMapping("/read-cookie")
    public ResponseEntity<String> readCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return ResponseEntity.ok("쿠키 값: " + cookie.getValue());
                }
            }
        }
        return ResponseEntity.status(404).body("쿠키를 찾을 수 없습니다");
    }

}
