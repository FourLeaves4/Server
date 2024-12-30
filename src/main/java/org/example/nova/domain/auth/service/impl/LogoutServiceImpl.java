package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.dto.response.LogoutResponseDto;
import org.example.nova.domain.auth.service.JwtService;
import org.example.nova.domain.auth.service.LogoutService;
import org.example.nova.global.security.jwt.JwtTokenProvider;
import org.example.nova.global.security.jwt.exception.CustomException;
import org.example.nova.global.security.jwt.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LogoutResponseDto logout(String token) {
        if (!token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "토큰 형식이 잘못되었습니다. 'Bearer '로 시작해야 합니다.");
        }
        String accessToken = token.substring(7);

        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }

        log.info("Logout successful");
        return new LogoutResponseDto("로그아웃에 성공했습니다.");
    }

    /*
    @Override
    public void logout(String token) {
        log.info("Token Received: {}", token);
        if (!jwtService.validateToken(token)) {
            log.error("Invalid Token: {}", token);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        log.info("Token is valid.");
    }

     */
}

