package org.example.nova.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private final String accessToken;
    private final String refreshToken;
}
