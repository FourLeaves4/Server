package org.example.nova.domain.home.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId;

    private Long userId;
    private int num;
    private int sum;

    @Column(columnDefinition = "JSON")
    private String month;
}