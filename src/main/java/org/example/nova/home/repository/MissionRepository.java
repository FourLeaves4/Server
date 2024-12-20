package org.example.nova.home.repository;

import org.example.nova.home.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface    MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findByUserId(Long userId);
}