package org.example.nova.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.nova.auth.entity.Member;
import org.example.nova.auth.entity.MemberRole;
import org.example.nova.auth.presentation.dto.JoinRequest;
import org.example.nova.auth.presentation.dto.LoginRequest;
import org.example.nova.auth.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return memberRepository.existsByLoginId(loginId);
    }

    public void join(JoinRequest joinRequest) {
        memberRepository.save(joinRequest.toEntity());
    }

    public Member login(LoginRequest loginRequest) {
        Member findMember = memberRepository.findByLoginId(loginRequest.getLoginId());

        if(findMember == null){
            return null;
        }

        if (!findMember.getPassword().equals(loginRequest.getPassword())) {
            return null;
        }

        return findMember;
    }

    public Member getLoginMemberById(Long memberId){
        if(memberId == null) return null;

        Optional<Member> findMember = memberRepository.findById(memberId);
        return findMember.orElse(null);

    }

    public void register(JoinRequest joinRequest) {
        String loginId = joinRequest.getLoginId();
        String password = joinRequest.getPassword();
        String name = joinRequest.getName();

        if (memberRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        Member newMember = Member.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .name(name)
                .role(MemberRole.USER)
                .build();

        memberRepository.save(new Member());
    }

    public Member authenticate(LoginRequest loginRequest) {
        String loginId = loginRequest.getLoginId();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByLoginId(loginId);

        if (member == null || !bCryptPasswordEncoder.matches(password, member.getPassword())) {
            return null;
        }

        return member;
    }
}
