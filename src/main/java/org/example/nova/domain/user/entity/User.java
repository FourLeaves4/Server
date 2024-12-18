package org.example.nova.domain.user.entity;


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
    private Long id;

    private String loginId;
    private String name;
    private String password;
    private String refreshToken;
    private String accessToken;
    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
