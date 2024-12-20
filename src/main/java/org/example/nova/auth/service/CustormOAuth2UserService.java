package org.example.nova.auth.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.auth.details.CustormOAuth2UserDetails;
import org.example.nova.auth.details.GoogleUserDetails;
import org.example.nova.user.entity.User;
import org.example.nova.user.entity.UserRole;
import org.example.nova.auth.info.OAuth2UserInfo;
import org.example.nova.user.repository.UserRepository;
import org.example.nova.home.entity.Mission;
import org.example.nova.home.repository.MissionRepository;
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
public class CustormOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;  // MissionRepository 주입

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();

        User findUser = userRepository.findByLoginId(loginId);
        User user;

        if (findUser == null) {
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);
            Mission mission = new Mission();
            mission.setUserId(user.getUserId());
            mission.setLevel(0);
            mission.setMissions(new String[0]);
            mission.setToday(new int[0]);
            missionRepository.save(mission);
        } else {
            user = findUser;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession();
            session.setAttribute("member", user);
        }

        return new CustormOAuth2UserDetails(user, oAuth2User.getAttributes());
    }
}