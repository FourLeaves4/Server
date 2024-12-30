package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.details.GoogleUserDetails;
import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.example.nova.domain.auth.service.OAuth2UserService;
import org.example.nova.domain.auth.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService implements OAuth2UserService {

    private final UserService userService;

    public OAuth2UserInfo extractUserInfo(OAuth2User oAuth2User, String provider) {
        if ("google".equals(provider)) {
            return new GoogleUserDetails(oAuth2User.getAttributes());
        }
        throw new IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: " + provider);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = extractUserInfo(oAuth2User, provider);

        try {
            userService.findOrCreateUser(userInfo, provider);
        } catch (Exception e) {
            log.error("Failed to save or find user: {}", e.getMessage());
            throw new RuntimeException("사용자 저장 또는 조회 중 오류가 발생했습니다.");
        }

        return oAuth2User;
    }


}
