package org.example.nova.auth.presentation;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.User;
import org.example.nova.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String loginRedirect() {
        return "redirect:/oauth2/authorization/google";
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

        return "redirect:/auth";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}