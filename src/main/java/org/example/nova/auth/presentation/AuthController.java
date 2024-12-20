package org.example.nova.auth.presentation;

import jakarta.servlet.http.HttpSession;
import org.example.nova.user.entity.User;
import org.example.nova.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/oauth2/authorization/google";
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<LoginResponseDto> getLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("member");
        if (loginUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(new LoginResponseDto(loginUser.getUserId()));
    }
}