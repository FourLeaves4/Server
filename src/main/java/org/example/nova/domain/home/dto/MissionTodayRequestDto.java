package org.example.nova.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissionTodayRequestDto {
    private int[] today;
}
