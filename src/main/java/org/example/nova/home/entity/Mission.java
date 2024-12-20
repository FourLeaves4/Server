package org.example.nova.home.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "mission")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int homeId;

    private Long userId;
    private int level;

    @Column(columnDefinition = "JSON")
    private String[] missions;

    @Column(columnDefinition = "JSON")
    private int[] today;
}