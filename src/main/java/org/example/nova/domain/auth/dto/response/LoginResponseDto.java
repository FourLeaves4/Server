package org.example.nova.domain.auth.dto.response;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private long userId;
    private String accessToken;
    private String refreshToken;

}