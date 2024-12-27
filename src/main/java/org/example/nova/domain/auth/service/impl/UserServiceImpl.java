package org.example.nova.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findOrCreateUser(OAuth2UserInfo userInfo, String provider) {
        System.out.println("Email: " + userInfo.getEmail());
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> {
                    System.out.println("User not found. Creating a new user.");
                    User newUser = new User();
                    newUser.setEmail(userInfo.getEmail());
                    newUser.setProvider(provider);
                    newUser.setProviderId(userInfo.getProviderId());
                    newUser.setName(userInfo.getName() != null ? userInfo.getName() : "Default Name");
                    newUser.setRole(UserRole.USER);
                    return userRepository.save(newUser);
                });
        System.out.println("User saved: " + user.getEmail());
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        System.out.println("Finding user by email: " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found with email: " + email);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });
    }

}
