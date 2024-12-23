package org.example.nova.home.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.nova.home.dto.*;
import org.example.nova.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home/{user_id}")
public class HomeController {

    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/mission")
    public MissionResponseDto getMission(@PathVariable("user_id") Long userId) throws JsonProcessingException {
        return homeService.getMissionByUserId(userId);
    }

    @PostMapping("/mission")
    public MissionResponseDto updateMission(@PathVariable("user_id") Long userId, @RequestBody MissionRequestDto missionRequestDto) throws JsonProcessingException {
        return homeService.getMissionByUserId(userId, missionRequestDto);
    }

    @PostMapping("/mission/value")
    public ResponseEntity<Void> updateMissionValue(@PathVariable("user_id") Long userId, @RequestBody MissionTodayRequestDto missionTodayRequestDto) throws JsonProcessingException {
        homeService.updateMission(userId, missionTodayRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ProfileResponseDto getProfile(@PathVariable("user_id") Long userId) {
        return homeService.getProfile(userId);
    }

    @GetMapping("/profile/level")
    public ProfileLevelResponseDto getProfileLevel(@PathVariable("user_id") Long userId) {
        return homeService.getProfileLevel(userId);
    }

    @GetMapping("/plan")
    public PlanResponseDto getPlan(@PathVariable("user_id") Long userId) {
        return homeService.getPlan(userId);
    }
}