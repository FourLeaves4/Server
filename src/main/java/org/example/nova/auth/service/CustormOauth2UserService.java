package org.example.nova.auth.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.auth.details.CustomOauth2UserDetails;
import org.example.nova.auth.details.GoogleUserDetails;
import org.example.nova.auth.entity.Member;
import org.example.nova.auth.entity.MemberRole;
import org.example.nova.auth.info.OAuth2UserInfo;
import org.example.nova.auth.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustormOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}",oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("google")){
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());

        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();

        Member findMember = memberRepository.findByLoginId(loginId);
        Member member;

        if (findMember == null) {
            member = Member.builder()
                    .loginId(loginId)
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .role(MemberRole.USER)
                    .build();
            memberRepository.save(member);
        } else{
            member = findMember;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession();
            session.setAttribute("member", member);
        }

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());
    }
}
