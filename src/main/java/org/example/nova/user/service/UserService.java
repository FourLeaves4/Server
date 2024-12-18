package org.example.nova.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.User;
import org.example.nova.user.entity.UserRole;
import org.example.nova.auth.presentation.LoginRequest;
import org.example.nova.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public User login(LoginRequest loginRequest) {
        User findUser = userRepository.findByLoginId(loginRequest.getLoginId());

        if(findUser == null){
            return null;
        }

        if (!findUser.getPassword().equals(loginRequest.getPassword())) {
            return null;
        }

        return findUser;
    }

    public User getLoginMemberById(Long memberId){
        if(memberId == null) return null;

        Optional<User> findMember = userRepository.findById(memberId);
        return findMember.orElse(null);

    }

    public User authenticate(LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        User user = userRepository.findByLoginId(loginId);

        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }
}
