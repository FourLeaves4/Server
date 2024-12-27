package org.example.nova.domain.auth.service;

import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserService {

    OAuth2User loadUser(OAuth2UserRequest userRequest);
    OAuth2UserInfo extractUserInfo(OAuth2User oAuth2User, String provider);

}
