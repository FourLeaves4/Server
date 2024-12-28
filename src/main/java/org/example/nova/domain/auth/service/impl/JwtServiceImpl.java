package org.example.nova.domain.auth.service.impl;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.nova.domain.auth.service.JwtService;
import org.example.nova.global.security.jwt.exception.CustomException;
import org.example.nova.global.security.jwt.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${JWT_SECRET}")
    private String secretKey;

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1 Hour
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 Day

    @Override
    public String generateAccessToken(String email) {
        if (secretKey.getBytes().length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 256 bits (32 bytes) long");
        }
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @Override
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public Date getAccessTokenExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    @Override
    public Date getRefreshTokenExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
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
