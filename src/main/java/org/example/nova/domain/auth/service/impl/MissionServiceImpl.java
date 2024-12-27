package org.example.nova.domain.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.nova.domain.auth.entity.User;
import org.example.nova.domain.auth.service.MissionService;
import org.example.nova.domain.home.entity.Mission;
import org.example.nova.domain.home.entity.Profile;
import org.example.nova.domain.home.repository.MissionRepository;
import org.example.nova.domain.home.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final ProfileRepository profileRepository;

    @Override
    public void initializeMissionAndProfile(User user) {
        Mission mission = new Mission();
        mission.setUserId(user.getUserId());
        mission.setLevel(1);
        missionRepository.save(mission);

        Profile profile = new Profile();
        profile.setUserId(user.getUserId());
        profileRepository.save(profile);
    }

}

