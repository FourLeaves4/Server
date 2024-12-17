package org.example.nova.user.repository;

import org.example.nova.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    User findByLoginId(String loginId);
}
