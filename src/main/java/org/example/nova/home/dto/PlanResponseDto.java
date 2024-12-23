package org.example.nova.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PlanResponseDto {
    private int avg;
    private List<Integer> week;
}