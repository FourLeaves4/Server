package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.details.GoogleUserDetails;
import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.example.nova.domain.auth.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2ServiceImpl implements OAuth2Service {

    private final RestTemplate restTemplate;

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${REDIRECT_URL}")
    private String redirectUri;

    @Override
    public String getGoogleAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("grant_type", "authorization_code");

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token",
                    params,
                    Map.class
            );
            log.info("Google Access Token: {}", response.getBody());
            return response.getBody().get("access_token").toString();
        } catch (Exception e) {
            log.error("Failed to fetch Google Access Token: {}", e.getMessage());
            throw new RuntimeException("Google Access Token 요청 실패");
        }
    }

    @Override
    public OAuth2UserInfo getGoogleUserInfo(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );
            log.info("Google User Info: {}", response.getBody());
            return new GoogleUserDetails(response.getBody());
        } catch (Exception e) {
            log.error("Failed to fetch Google User Info: {}", e.getMessage());
            throw new RuntimeException("Google 사용자 정보 요청 실패");
        }
    }
}
