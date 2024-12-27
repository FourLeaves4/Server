package org.example.nova.auth.presentation;

import jakarta.servlet.http.HttpSession;
import org.example.nova.auth.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping
    public ResponseEntity<LoginResponseDto> getLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("member");
        if (loginUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new LoginResponseDto(loginUser.getUserId()));
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginRedirect() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "/oauth2/authorization/google")
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
    }
}

