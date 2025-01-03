package org.example.nova.home.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.nova.home.dto.MissionRequestDto;
import org.example.nova.home.dto.MissionResponseDto;
import org.example.nova.home.dto.MissionTodayRequestDto;
import org.example.nova.home.dto.ProfileResponseDto;
import org.example.nova.home.dto.ProfileLevelResponseDto;
import org.example.nova.home.dto.PlanResponseDto;
import org.example.nova.home.entity.Mission;
import org.example.nova.home.entity.Profile;
import org.example.nova.home.repository.MissionRepository;
import org.example.nova.home.repository.ProfileRepository;
import org.example.nova.auth.entity.User;
import org.example.nova.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HomeService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public HomeService(MissionRepository missionRepository, UserRepository userRepository, ProfileRepository profileRepository) {
        this.missionRepository = missionRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public MissionResponseDto getMissionByUserId(Long userId) throws JsonProcessingException {
        Optional<Mission> home = missionRepository.findByUserId(userId);
        Optional<User> user = userRepository.findByUserId(userId);

        if (home.isPresent() && user.isPresent()) {
            Mission mission = home.get();
            User u = user.get();

            List<String> missionsList = new ArrayList<>();
            if (mission.getMissions() != null && !mission.getMissions().isEmpty()) {
                missionsList = Arrays.asList(objectMapper.readValue(mission.getMissions(), String[].class));
            }

            List<Integer> todayList = new ArrayList<>();
            if (mission.getToday() != null && !mission.getToday().isEmpty()) {
                todayList = Arrays.asList(objectMapper.readValue(mission.getToday(), Integer[].class));
            }

            return new MissionResponseDto(missionsList, todayList, mission.getLevel(), u.getName());
        } else {
            throw new RuntimeException("Mission or User not found for user_id: " + userId);
        }
    }

    public MissionResponseDto getMissionByUserId(Long userId, MissionRequestDto missionRequestDto) throws JsonProcessingException {
        Optional<Mission> home = missionRepository.findByUserId(userId);
        Optional<User> user = userRepository.findByUserId(userId);

        String[] A = {"😀 js에 대해 공부하기", "📘 React에 대해 공부하기", "💻 FE에 대해 탐색하기", "✏️ 코딩테스트 1개 풀기", "📑 TIL 올리기"};
        String[] B = {"😀 java에 대해 공부하기", "📘 Spring에 대해 공부하기", "💻 BE에 대해 탐색하기", "✏️ 코딩테스트 1개 풀기", "📑 TIL 올리기"};
        String[] C = {"😀 swift에 대해 공부하기", "📘 UI 구성 공부하기", "💻 IOS에 대해 탐색하기", "✏️ 코딩테스트 1개 풀기", "📑 TIL 올리기"};
        String[] D = {"😀 kotlin에 대해 공부하기", "📘 Compose 구성 공부하기", "💻 AOS에 대해 탐색하기", "✏️ 코딩테스트 1개 풀기", "📑 TIL 올리기"};
        String[] N = {"😀 전공 조사하기", "📘 개발 언어 선택하기", "💻 개발 환경 설치하기", "🐱‍ Git에 대해 공부하기", "📑 TIL 올리기"};

        if (home.isPresent() && user.isPresent()) {
            Mission mission = home.get();
            User u = user.get();

            if (missionRequestDto.getMajor() != 0) {
                u.setMajor(missionRequestDto.getMajor());
                userRepository.save(u);
            }

            String[] missions;
            switch (u.getMajor()) {
                case 1 -> missions = A;
                case 2 -> missions = B;
                case 3 -> missions = C;
                case 4 -> missions = D;
                case 5 -> missions = N;
                default -> throw new RuntimeException("Invalid major value: " + u.getMajor());
            }

            mission.setMissions(objectMapper.writeValueAsString(missions));

            if (mission.getToday() == null || mission.getToday().isEmpty()) {
                mission.setToday(objectMapper.writeValueAsString(new int[]{0, 0, 0, 0, 0}));
            }

            missionRepository.save(mission);

            List<String> missionsList = Arrays.asList(objectMapper.readValue(mission.getMissions(), String[].class));
            List<Integer> todayList = Arrays.asList(objectMapper.readValue(mission.getToday(), Integer[].class));

            return new MissionResponseDto(missionsList, todayList, mission.getLevel(), u.getName());
        } else {
            throw new RuntimeException("Mission or User not found for user_id: " + userId);
        }
    }

    public void updateMission(Long userId, MissionTodayRequestDto missionTodayRequestDto) throws JsonProcessingException {
        Optional<Mission> home = missionRepository.findByUserId(userId);

        if (home.isPresent()) {
            Mission mission = home.get();

            String todayJson = objectMapper.writeValueAsString(missionTodayRequestDto.getToday());
            mission.setToday(todayJson);
            missionRepository.save(mission);

            Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);
            if (optionalProfile.isPresent()) {
                Profile profile = optionalProfile.get();

                List<Integer> month = parseMonth(profile.getMonth());
                int todayIndex = LocalDate.now().getDayOfMonth() - 1;

                List<Integer> todayList = objectMapper.readValue(mission.getToday(), List.class);
                int todayCount = (int) todayList.stream().filter(value -> value == 1).count();

                if (month.get(todayIndex) == null) {
                    month.set(todayIndex, 0);
                }
                month.set(todayIndex, todayCount);

                String updatedMonthJson = objectMapper.writeValueAsString(month);
                profile.setMonth(updatedMonthJson);
                profileRepository.save(profile);
            } else {
                throw new RuntimeException("Profile not found for user_id: " + userId);
            }
        } else {
            throw new RuntimeException("Mission not found for user_id: " + userId);
        }
    }


    public ProfileResponseDto getProfile(Long userId) {
        Optional<Profile> optionalProfile = profileRepository.findByUserId(userId);
        Profile profile = optionalProfile.orElseGet(() -> createNewProfile(userId));
        List<Integer> month = parseMonth(profile.getMonth());

        profile.setSum(month.stream().mapToInt(Integer::intValue).sum());
        profile.setNum(profile.getSum() % 6);
        int level = (profile.getSum() / 5)+1;

        profile.setMonth(month.toString());
        profileRepository.save(profile);

        Optional<User> optionalUser = userRepository.findById(userId);
        String name = optionalUser.map(User::getName).orElse("Unknown User");
        String email = optionalUser.map(User::getEmail).orElse("Uknown User");

        Mission mission = missionRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Mission 데이터가 존재하지 않습니다. userId: " + userId));
        mission.setLevel(level);
        missionRepository.save(mission);

        return new ProfileResponseDto(month, profile.getNum(), profile.getSum(), level, name, email);
    }


    private Profile createNewProfile(Long userId) {
        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setNum(0);
        profile.setSum(0);

        List<Integer> month = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            month.add(0);
        }
        profile.setMonth(month.toString());

        return profileRepository.save(profile);
    }

    private List<Integer> parseMonth(String monthJson) {
        try {
            if (monthJson != null) {
                return objectMapper.readValue(monthJson, List.class);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Integer> month = new ArrayList<>(31);
        for (int i = 0; i < 31; i++) {
            month.add(0);
        }
        return month;
    }

    public ProfileLevelResponseDto getProfileLevel(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user_id: " + userId));
        Mission mission= missionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user_id: " + userId));
        return new ProfileLevelResponseDto(profile.getNum(), mission.getLevel());
    }

    public PlanResponseDto getPlan(Long userId) {
        Optional<Profile> profile = profileRepository.findByUserId(userId);

        if (profile.isEmpty()) {
            throw new RuntimeException("Profile not found for userId: " + userId);
        }

        List<Integer> monthData;
        try {
            monthData = objectMapper.readValue(profile.get().getMonth(), new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse month data: " + e.getMessage(), e);
        }

        List<Integer> weekData = getWeekData(monthData);

        List<Integer> weekDataMultiplied = new ArrayList<>();
        for (int day : weekData) {
            weekDataMultiplied.add(day * 20);
        }

        int avg = (int) weekDataMultiplied.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        return new PlanResponseDto(avg, weekDataMultiplied);
    }

    private List<Integer> getWeekData(List<Integer> monthData) {
        LocalDate now = LocalDate.now();
        int todayIndex = now.getDayOfMonth() - 1;
        int startIndex = todayIndex - now.getDayOfWeek().getValue() + 1;
        int endIndex = startIndex + 7;

        startIndex = Math.max(startIndex, 0);
        endIndex = Math.min(endIndex, monthData.size());

        List<Integer> weekData = new ArrayList<>(monthData.subList(startIndex, endIndex));

        while (weekData.size() < 7) {
            weekData.add(0);
        }
        return weekData;
    }
}