package org.example.nova.home.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponseDto {
    private int[] month;
    private int num;
    private int sum;
    private int level;
    private String name;
    private String email;
}
