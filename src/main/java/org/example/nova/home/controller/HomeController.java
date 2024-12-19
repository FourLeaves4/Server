package org.example.nova.home.controller;

import org.example.nova.home.dto.MissionRequestDto;
import org.example.nova.home.dto.MissionTodayRequestDto;
import org.example.nova.home.dto.MissionResponseDto;
import org.example.nova.home.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public MissionResponseDto getMission(@PathVariable("user_id") Long userId, @RequestBody MissionRequestDto missionRequestDto) {
        return homeService.getMissionByUserId(userId, missionRequestDto);
    }

    @PostMapping("/mission")
    public String updateMission(@PathVariable("user_id") Long userId, @RequestBody MissionTodayRequestDto missionTodayRequestDto) {
        homeService.updateMission(userId, missionTodayRequestDto);
        return "Mission updated successfully for user_id: " + userId;
    }
}