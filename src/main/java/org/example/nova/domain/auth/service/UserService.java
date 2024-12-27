package org.example.nova.domain.auth.service;

import org.example.nova.domain.auth.entity.User;
import org.example.nova.domain.auth.info.OAuth2UserInfo;

public interface UserService {

    User findOrCreateUser(OAuth2UserInfo userInfo, String provider);
    User findUserByEmail(String email);

}
