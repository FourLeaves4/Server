package org.example.nova.global.verifier;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleTokenVerifier {

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/tokeninfo";

    public boolean verifyToken(String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = GOOGLE_TOKEN_URL + "?id_token=" + token;
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
