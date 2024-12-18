package org.example.nova.domain.auth.presentation;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.global.token.TokenService;
import org.example.nova.domain.user.entity.User;
import org.example.nova.domain.user.repository.UserRepository;
import org.example.nova.domain.user.service.UserService;
import org.example.nova.global.verifier.GoogleTokenVerifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final TokenService tokenService;

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        refreshToken = refreshToken.replace("Bearer ", "");

        User user = userRepository.findByRefreshToken(refreshToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 refresh token");
        }

        try {
            String newAccessToken = tokenService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        } catch (Exception e) {
            log.error("Failed to refresh access token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 갱신 실패");
        }

    }

    @GetMapping
    public String home(Model model, HttpSession session) {
        String loginType = "auth";
        String pageName = "홈 화면";
        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", pageName);

        User user = (User) session.getAttribute("member");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }

        return "home";  // home.html
    }

    @GetMapping("/login")
    public String loginRedirect() {
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");

        if (!googleTokenVerifier.verifyToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByAccessToken(accessToken);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        String loginType = "auth";
        String pageName = "관리자 페이지";
        User user = (User) session.getAttribute("member");

        return "redirect:/oauth";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/oauth";
    }
}
