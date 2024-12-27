package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.nova.domain.auth.service.ReissueService;
import org.example.nova.global.security.jwt.dto.ReissueTokenResponseDto;
import org.example.nova.global.security.jwt.exception.CustomException;
import org.example.nova.global.security.jwt.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class ReissueServiceImpl implements ReissueService {

    private final JwtServiceImpl jwtService;

    public ReissueTokenResponseDto reissue(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(email);
        String newRefreshToken = jwtService.generateRefreshToken(email);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String accessTokenExpiry = dateFormat.format(jwtService.getAccessTokenExpiration(newAccessToken));
        String refreshTokenExpiry = dateFormat.format(jwtService.getRefreshTokenExpiration(newRefreshToken));

        return new ReissueTokenResponseDto(
                newAccessToken,
                accessTokenExpiry,
                newRefreshToken,
                refreshTokenExpiry
        );
    }

}
