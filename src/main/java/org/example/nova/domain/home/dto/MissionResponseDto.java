package org.example.nova.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class MissionResponseDto {
    private List<String> mission;
    private List<Integer> today;
    private int level;
    private String name;

}