package org.example.nova.domain.user.repository;

import org.example.nova.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    User findByLoginId(String loginId);
    User findByRefreshToken(String refreshToken);
    User findByAccessToken(String accessToken);
}
