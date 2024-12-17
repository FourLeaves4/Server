package org.example.nova.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.User;
import org.example.nova.user.entity.UserRole;
import org.example.nova.auth.presentation.dto.JoinRequest;
import org.example.nova.auth.presentation.dto.LoginRequest;
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

    public void join(JoinRequest joinRequest) {
        userRepository.save(joinRequest.toEntity());
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

    public void register(JoinRequest joinRequest) {
        String loginId = joinRequest.getLoginId();
        String password = joinRequest.getPassword();
        String name = joinRequest.getName();

        if (userRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        User newUser = User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .role(UserRole.USER)
                .build();

        userRepository.save(new User());
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
