package org.example.nova.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String loginId;
    private String name;
    private String password;
    private int major;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String provider;

    private String providerId;
}