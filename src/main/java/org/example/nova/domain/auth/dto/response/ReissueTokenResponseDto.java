package org.example.nova.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReissueTokenResponseDto {

    private String accessToken;
    private Date accessTokenExpiry;
    private String refreshToken;
    private Date refreshTokenExpiry;

}

