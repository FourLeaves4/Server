package org.example.nova.home.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
@Entity
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    private Long userId;
    private int level;

    @Column(columnDefinition = "JSON")
    private String missions;

    @Column(columnDefinition = "JSON")
    private String today;

    public void setMissions(String[] missionsArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.missions = objectMapper.writeValueAsString(missionsArray);
    }

    public void setToday(int[] todayArray) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.today = objectMapper.writeValueAsString(todayArray);
    }
}
