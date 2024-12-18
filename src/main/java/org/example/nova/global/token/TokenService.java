package org.example.nova.global.token;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final String googleTokenUrl = "https://oauth2.googleapis.com/token";

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String refreshAccessToken(String refreshToken) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("refresh_token", refreshToken);
        requestBody.put("grant_type", "refresh_token");

        try {
            Map<String, Object> response = restTemplate.postForObject(googleTokenUrl, requestBody, Map.class);
            return (String) response.get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Access token 갱신 실패", e);
        }
    }

}
