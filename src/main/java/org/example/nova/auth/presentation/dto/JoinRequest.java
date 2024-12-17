package org.example.nova.auth.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.nova.auth.entity.Member;
import org.example.nova.auth.entity.MemberRole;

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

    public Member toEntity(){
        return Member.builder()
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .role(MemberRole.USER)
                .build();
    }
}
