package org.example.nova.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.nova.auth.entity.User;
import org.example.nova.auth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + name));
    }

    public User authenticate(String name, String password) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + name));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return user;
    }
}
