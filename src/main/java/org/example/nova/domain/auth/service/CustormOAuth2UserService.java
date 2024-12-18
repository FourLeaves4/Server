package org.example.nova.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.details.CustormOAuth2UserDetails;
import org.example.nova.domain.auth.details.GoogleUserDetails;
import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.example.nova.domain.user.entity.User;
import org.example.nova.domain.user.entity.UserRole;
import org.example.nova.domain.user.repository.UserRepository;
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
        log.info("Starting OAuth2 User Login Process");

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            log.info("Received OAuth2 User Attributes: {}", oAuth2User.getAttributes());

            // OAuth2 Client Information
            String clientRegistrationId = userRequest.getClientRegistration().getRegistrationId();
            String accessToken = userRequest.getAccessToken().getTokenValue();
            log.info("Client Registration ID: {}", clientRegistrationId);
            log.info("Access Token: {}", accessToken);

            // SecurityContext에서 현재 사용자 가져오기
            String principalName = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("Principal Name: {}", principalName);

            OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService
                    .loadAuthorizedClient(
                            userRequest.getClientRegistration().getRegistrationId(),
                            null  // principalName 대신 null을 사용
                    );

            String refreshToken = null;
            if (oAuth2AuthorizedClient != null && oAuth2AuthorizedClient.getRefreshToken() != null) {
                refreshToken = oAuth2AuthorizedClient.getRefreshToken().getTokenValue();
                log.info("Refresh Token: {}", refreshToken);
            } else {
                log.warn("Refresh Token is null");
            }


            // Extract User Information
            OAuth2UserInfo oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
            String provider = clientRegistrationId;
            String providerId = oAuth2UserInfo.getProviderId();
            String loginId = provider + "_" + providerId;
            String name = oAuth2UserInfo.getName();

            log.info("OAuth2 User Info - Provider: {}, Provider ID: {}, Login ID: {}, Name: {}",
                    provider, providerId, loginId, name);

            // Save or Update User
            User user = userRepository.findByLoginId(loginId);
            if (user == null) {
                user = User.builder()
                        .loginId(loginId)
                        .name(name)
                        .provider(provider)
                        .providerId(providerId)
                        .refreshToken(refreshToken)
                        .role(UserRole.USER)
                        .build();
                log.info("Saving new user to the database: {}", user);
            } else {
                log.info("Updating existing user: {}", user);
                user.setRefreshToken(refreshToken);
            }

            userRepository.save(user);
            log.info("User successfully saved or updated: {}", user);

            return new CustormOAuth2UserDetails(user, oAuth2User.getAttributes());

        } catch (Exception e) {
            log.error("Error during OAuth2 login process", e);
            throw new OAuth2AuthenticationException("Failed to process OAuth2 login: " + e.getMessage());
        }
    }
}
