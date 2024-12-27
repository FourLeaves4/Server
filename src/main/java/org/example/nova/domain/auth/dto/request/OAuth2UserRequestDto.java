package org.example.nova.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserRequestDto {

    private String provider;
    private Map<String, Object> attributes;

}
