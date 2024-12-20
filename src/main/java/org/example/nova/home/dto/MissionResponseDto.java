package org.example.nova.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissionResponseDto {
    private String[] mission;
    private int[] today;
    private int level;
    private String name;

}
