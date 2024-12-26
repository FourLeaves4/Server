package org.example.nova.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void storeRefreshToken(String username, String token, LocalDateTime expirationDate) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setToken(token);
        refreshToken.setExpirationDate(expirationDate);
        refreshTokenRepository.save(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> storedToken = refreshTokenRepository.findByToken(token);
        return storedToken.isPresent() && storedToken.get().getExpirationDate().isAfter(LocalDateTime.now());
    }

    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
}
