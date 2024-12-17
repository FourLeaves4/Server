package org.example.nova.auth.presentation;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.User;
import org.example.nova.auth.presentation.dto.JoinRequest;
import org.example.nova.auth.presentation.dto.LoginRequest;
import org.example.nova.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping
    public String home(Model model, HttpSession session) {
        String loginType = "oauth";
        String pageName = "홈 화면";
        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", pageName);

        User user = (User) session.getAttribute("member");
        if (user != null) {
            model.addAttribute("name", user.getName());
        }

        return "home";  // home.html
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        String loginType = "oauth";
        String pageName = "회원 가입";
        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", pageName);
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";  // join.html
    }

    @PostMapping("/join")
    public String join(JoinRequest joinRequest) {
        userService.register(joinRequest);
        return "redirect:/oauth/login";  // 로그인 페이지로 리다이렉트
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        String loginType = "oauth";
        String pageName = "로그인";
        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", pageName);
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";  // login.html
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession session, Model model) {
        User user = userService.authenticate(loginRequest);
        if (user != null) {
            session.setAttribute("member", user);
            return "redirect:/oauth";  // 홈 페이지로 리다이렉트
        } else {
            model.addAttribute("globalError", "ID 또는 비밀번호가 잘못되었습니다.");
            model.addAttribute("loginType", "oauth");
            model.addAttribute("pageName", "로그인");
            return "login";  // login.html
        }
    }

    @GetMapping("/info")
    public String info(Model model, HttpSession session) {
        String loginType = "oauth";
        String pageName = "마이 페이지";
        User user = (User) session.getAttribute("member");

        if (user == null) {
            return "redirect:/oauth/login";
        }

        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", pageName);
        model.addAttribute("member", user);
        return "info";  // info.html
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        String loginType = "oauth";
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
