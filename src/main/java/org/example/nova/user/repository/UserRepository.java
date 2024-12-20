package org.example.nova.user.repository;

import org.example.nova.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    User findByLoginId(String loginId);
    Optional<User> findByUserId(Long userId);
}