package org.example.nova.domain.auth.service;

import org.example.nova.domain.auth.dto.response.LogoutResponseDto;

public interface LogoutService {

    LogoutResponseDto logout(String token);

}

