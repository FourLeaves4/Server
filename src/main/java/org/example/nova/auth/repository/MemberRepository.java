package org.example.nova.auth.repository;

import org.example.nova.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByLoginId(String loginId);
    Member findByLoginId(String loginId);
}
