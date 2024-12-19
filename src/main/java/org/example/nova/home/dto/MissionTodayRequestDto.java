package org.example.nova.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissionTodayRequestDto {
    private int[] today;
}
