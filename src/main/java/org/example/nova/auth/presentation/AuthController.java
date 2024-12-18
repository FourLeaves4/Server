package org.example.nova.auth.presentation;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.User;
import org.example.nova.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

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
    public String loginForm(Model model) {
        String loginType = "auth";
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
            return "redirect:/auth";  // 홈 페이지로 리다이렉트
        } else {
            model.addAttribute("globalError", "ID 또는 비밀번호가 잘못되었습니다.");
            model.addAttribute("loginType", "auth");
            model.addAttribute("pageName", "로그인");
            return "login";  // login.html
        }
    }

    @GetMapping("/info")
    public String info(Model model, HttpSession session) {
        String loginType = "auth";
        String pageName = "마이 페이지";
        User user = (User) session.getAttribute("member");

        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("loginType", loginType);
        model.addAttribute("pageName", pageName);
        model.addAttribute("member", user);
        return "info";  // info.html
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
