package org.example.nova.domain.auth.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/custom-error")
public class ErrorController {
    @RequestMapping
    public ResponseEntity<String> handleError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패: 잘못된 요청입니다.");
    }
}

