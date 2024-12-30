package org.example.nova.domain.auth.service.impl;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.nova.domain.auth.service.JwtService;
import org.example.nova.global.security.jwt.exception.CustomException;
import org.example.nova.global.security.jwt.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final String KEY = "${JWT_SECRET}";
    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1시간
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

    @Override
    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Date getAccessTokenExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public Date getRefreshTokenExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }
        return header.substring(7);
    }

}



/*
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1시간
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

    @Override
    public String generateAccessToken(String email) {
        System.out.println("Generating access token for email: " + email);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(secretKey) // SecretKey 사용
                .compact();
    }

    @Override
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(secretKey) // SecretKey 사용
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // SecretKey 사용
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // SecretKey 사용
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Date getAccessTokenExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // SecretKey 사용
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public Date getRefreshTokenExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // SecretKey 사용
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public String extractToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return header.substring(7);
    }
}

 */
