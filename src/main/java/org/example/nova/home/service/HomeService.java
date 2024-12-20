package org.example.nova.home.service;

import org.example.nova.home.dto.MissionRequestDto;
import org.example.nova.home.dto.MissionTodayRequestDto;
import org.example.nova.home.dto.MissionResponseDto;
import org.example.nova.home.entity.Mission;
import org.example.nova.home.repository.MissionRepository;
import org.example.nova.user.repository.UserRepository;
import org.example.nova.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeService(MissionRepository missionRepository, UserRepository userRepository) {
        this.missionRepository = missionRepository;
        this.userRepository = userRepository;
    }

    public MissionResponseDto getMissionByUserId(Long userId, MissionRequestDto missionRequestDto) {
        Optional<Mission> home = missionRepository.findByUserId(userId);
        Optional<User> user = userRepository.findByUserId(userId);

        String[] A = {"js에 대해 공부하기", "React에 대해 공부하기", "front-end에 대해 탐색하기", "코딩테스트 1개 풀기", "TIL 올리기"};
        String[] B = {"java에 대해 공부하기", "Spring에 대해 공부하기", "back-end에 대해 탐색하기", "코딩테스트 1개 풀기", "TIL 올리기"};
        String[] C = {"swift에 대해 공부하기", "Xcode에 대해 공부하기", "ios에 대해 탐색하기", "코딩테스트 1개 풀기", "TIL 올리기"};
        String[] D = {"kotlin에 대해 공부하기", "Android Studio에 대해 공부하기", "aos에 대해 탐색하기", "코딩테스트 1개 풀기", "TIL 올리기"};
        String[] N = {"전공 조사하기", "개발 언어 선택하기", "개발 환경 설치하기", "깃과 깃허브에 대한 공부하기", "TIL 올리기"};

        if (home.isPresent() && user.isPresent()) {
            Mission m = home.get();
            User u = user.get();

            if (missionRequestDto.getMajor() != 0) {
                u.setMajor(missionRequestDto.getMajor()); // major 값 업데이트
                userRepository.save(u); // DB에 저장
            }

            String[] missions;
            switch (u.getMajor()) {
                case 1:
                    missions = A;
                    break;
                case 2:
                    missions = B;
                    break;
                case 3:
                    missions = C;
                    break;
                case 4:
                    missions = D;
                    break;
                case 5:
                    missions = N;
                    break;
                default:
                    throw new RuntimeException("Invalid major value: " + u.getMajor());
            }

            m.setMissions(missions);

            int[] today = m.getToday();
            if (today == null || today.length == 0) {
                today = new int[]{0, 0, 0, 0, 0};
                m.setToday(today);
            }

            missionRepository.save(m);

            return new MissionResponseDto(
                    m.getMissions(),
                    m.getToday(),
                    m.getLevel(),
                    u.getName()
            );
        } else {
            throw new RuntimeException("Mission or User not found for user_id: " + userId);
        }
    }

    public void updateMission(Long userId, MissionTodayRequestDto missionTodayRequestDto) {
        Optional<Mission> home = missionRepository.findByUserId(userId);

        if (home.isPresent()) {
            Mission h = home.get();
            h.setToday(missionTodayRequestDto.getToday());
            missionRepository.save(h);
        } else {
            throw new RuntimeException("Home not found for user_id: " + userId);
        }
    }


}