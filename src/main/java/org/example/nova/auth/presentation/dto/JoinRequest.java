package org.example.nova.auth.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.nova.user.entity.User;
import org.example.nova.user.entity.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {
    @NotBlank(message = "ID를 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    public User toEntity(){
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .role(UserRole.USER)
                .build();
    }
}
