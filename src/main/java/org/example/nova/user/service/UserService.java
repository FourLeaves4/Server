package org.example.nova.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.User;
import org.example.nova.auth.presentation.LoginRequestDto;
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

    public User login(LoginRequestDto loginRequestDto) {
        User findUser = userRepository.findByLoginId(loginRequestDto.getLoginId());

        if(findUser == null){
            return null;
        }

        if (!findUser.getPassword().equals(loginRequestDto.getPassword())) {
            return null;
        }

        return findUser;
    }

    public User getLoginMemberById(Long memberId){
        if(memberId == null) return null;

        Optional<User> findMember = userRepository.findById(memberId);
        return findMember.orElse(null);

    }

    public User authenticate(LoginRequestDto loginRequestDto) {
        String loginId = loginRequestDto.getLoginId();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByLoginId(loginId);

        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        return user;
    }
}
