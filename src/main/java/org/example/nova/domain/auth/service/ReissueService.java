package org.example.nova.domain.auth.service;

import org.example.nova.global.security.jwt.dto.ReissueTokenResponseDto;

public interface ReissueService {

    ReissueTokenResponseDto reissue(String refreshToken);

}
