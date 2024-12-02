package nova.auth.controller;

import nova.auth.dto.MajorRequestDto;
import nova.auth.dto.MajorResponseDto;
import nova.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/major")
    public ResponseEntity<MajorResponseDto> submitSurvey(@RequestBody MajorRequestDto response) {
        MajorResponseDto result = authService.calculateSurveyResult(response);
        return ResponseEntity.ok(result);
    }

}
