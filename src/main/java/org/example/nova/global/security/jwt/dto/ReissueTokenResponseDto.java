package org.example.nova.global.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueTokenResponseDto {

    private final String accessToken;
    private final String accessTokenExpiresIn;
    private final String refreshToken;
    private final String refreshTokenExpiresIn;

}