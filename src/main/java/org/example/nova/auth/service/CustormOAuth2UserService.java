package org.example.nova.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.nova.auth.details.CustormOAuth2UserDetails;
import org.example.nova.auth.details.GoogleUserDetails;
import org.example.nova.home.entity.Profile;
import org.example.nova.user.entity.User;
import org.example.nova.user.entity.UserRole;
import org.example.nova.auth.info.OAuth2UserInfo;
import org.example.nova.user.repository.UserRepository;
import org.example.nova.home.entity.Mission;
import org.example.nova.home.repository.MissionRepository;
import org.example.nova.home.repository.ProfileRepository;
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
    private final MissionRepository missionRepository;
    private final ProfileRepository profileRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            // 새로운 사용자 생성
            user = User.builder()
                    .loginId(loginId)
                    .name(name)
                    .provider(provider)
                    .providerId(providerId)
                    .role(UserRole.USER)
                    .build();
            userRepository.save(user);

            // 새로운 Mission 생성
            Mission mission = new Mission();
            mission.setUserId(user.getUserId());
            mission.setLevel(0);

            try {
                mission.setMissions(objectMapper.writeValueAsString(new Object()));
                mission.setToday(objectMapper.writeValueAsString(new Object()));
            } catch (JsonProcessingException e) {
                log.error("Error serializing missions or today: ", e);
            }
            missionRepository.save(mission);

            Profile profile = new Profile();
            profile.setUserId(user.getUserId());
            profile.setNum(0);
            profile.setSum(0);
            try {
                profile.setMonth(objectMapper.writeValueAsString(new Object())); // 빈 JSON 객체로 초기화
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            profileRepository.save(profile);

        } else {
            // 기존 사용자 처리
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