package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.domain.auth.entity.User;
import org.example.nova.domain.auth.entity.UserRole;
import org.example.nova.domain.auth.info.OAuth2UserInfo;
import org.example.nova.domain.auth.repository.UserRepository;
import org.example.nova.domain.auth.service.UserService;
import org.example.nova.global.security.jwt.exception.CustomException;
import org.example.nova.global.security.jwt.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findOrCreateUser(OAuth2UserInfo userInfo, String provider) {
        return userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> {
                    log.info("User not found. Creating new user: {}", userInfo.getEmail());
                    User newUser = new User();
                    newUser.setEmail(userInfo.getEmail());
                    newUser.setProvider(provider);
                    newUser.setProviderId(userInfo.getProviderId());
                    newUser.setName(userInfo.getName());
                    newUser.setRole(UserRole.USER);
                    return userRepository.save(newUser);
                });
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
