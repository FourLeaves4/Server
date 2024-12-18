package org.example.nova.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "user")
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

    @Column(name = "access_token")
    private String accessToken;

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}
