package org.example.nova.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ProfileResponseDto {
    private List<Integer> month;
    private int num;
    private int sum;
    private int level;
    private String name;
    private String email;
}