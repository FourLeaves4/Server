package org.example.nova.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.details.CustormOAuth2UserDetails;
import org.example.nova.domain.auth.details.GoogleUserDetails;
import org.example.nova.domain.user.entity.User;
import org.example.nova.domain.user.entity.UserRole;
import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.example.nova.domain.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustormOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("getAttributes : {}", oAuth2User.getAttributes());

        OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService
                .loadAuthorizedClient(
                        userRequest.getClientRegistration().getRegistrationId(),
                        SecurityContextHolder.getContext().getAuthentication().getName()
                );

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String refreshToken = oAuth2AuthorizedClient != null && oAuth2AuthorizedClient
                .getRefreshToken() != null ? oAuth2AuthorizedClient.getRefreshToken().getTokenValue() : null;

        if (accessToken == null || refreshToken == null) {
            log.error("Access token or refresh token is null");
            throw new IllegalArgumentException("Access token or refresh token is null");
        }

        OAuth2UserInfo oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        String loginId = userRequest.getClientRegistration().getRegistrationId() + "_" + oAuth2UserInfo.getProviderId();

        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            user = User.builder()
                    .loginId(loginId)
                    .name(oAuth2UserInfo.getName())
                    .provider(userRequest.getClientRegistration().getRegistrationId())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .refreshToken(refreshToken)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
        } else {
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }

        return new CustormOAuth2UserDetails(user, oAuth2User.getAttributes());
    }
}