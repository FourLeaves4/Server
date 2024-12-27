package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.nova.domain.auth.service.JwtService;
import org.example.nova.domain.auth.service.LogoutService;
import org.example.nova.global.security.jwt.exception.CustomException;
import org.example.nova.global.security.jwt.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtService jwtService;

    @Override
    public void logout(String token) {
        if (!jwtService.validateToken(token)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}

