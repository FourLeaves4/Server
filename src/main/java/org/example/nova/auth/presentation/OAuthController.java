package org.example.nova.auth.presentation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {

    // OAuth2 인증 성공 후 사용자 정보 처리
    @GetMapping("/oauth-success")
    public ResponseEntity<String> oauthSuccess(@RequestParam String code) {
        // Google에서 인증 코드(code)를 받아 필요한 로직 수행
        return ResponseEntity.ok("OAuth2 인증 성공. 인증 코드: " + code);
    }

    // OAuth2 인증 실패
    @GetMapping("/oauth-failure")
    public ResponseEntity<String> oauthFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OAuth2 인증 실패");
    }
}
