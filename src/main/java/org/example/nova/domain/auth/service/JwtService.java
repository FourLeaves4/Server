package org.example.nova.domain.auth.service;

import java.util.Date;

public interface JwtService {

    String generateAccessToken(String email);
    String generateRefreshToken(String email);
    String extractToken(String header);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    Date getAccessTokenExpiration(String token);
    Date getRefreshTokenExpiration(String token);

}
