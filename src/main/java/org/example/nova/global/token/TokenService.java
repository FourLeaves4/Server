package org.example.nova.global.token;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final String googleTokenUrl = "https://oauth2.googleapis.com/token";
    private final RestTemplate restTemplate = new RestTemplate();

    public String refreshAccessToken(String refreshToken) {
        Map<String, String> requstBody = new HashMap<>();
        requstBody.put("client_id", "${GOOGLE_CLIENT_ID}");
        requstBody.put("client_secret", "${GOOGLE_CLIENT_SECRET}");
        requstBody.put("refresh_token", refreshToken);
        requstBody.put("grant_type", "refresh_token");

        try {
            Map<String, Object> response = restTemplate.postForObject(googleTokenUrl, requstBody, Map.class);
            return (String) response.get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("access token을 불러오는데 실패했습니다.", e);
        }
    }

}
