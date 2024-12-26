package org.example.nova.auth.repository;

import org.example.nova.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginId(String loginId);
    Optional<User> findByUserId(Long userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
}