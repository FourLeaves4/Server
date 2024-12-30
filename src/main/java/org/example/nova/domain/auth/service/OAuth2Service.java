package org.example.nova.domain.auth.service;

import org.example.nova.domain.auth.info.OAuth2UserInfo;

public interface OAuth2Service {

    OAuth2UserInfo getGoogleUserInfo(String token);

    String getGoogleAccessToken(String code);

}
