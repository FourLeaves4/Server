package org.example.nova.domain.auth.config;


import lombok.RequiredArgsConstructor;
import org.example.nova.domain.user.entity.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/admin").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/auth/info").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(auth -> auth
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/loginProc")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/auth")
                        .failureUrl("/auth")
                        .permitAll()
                )
                .oauth2Login(auth -> auth
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/auth", true)
                        .failureUrl("/auth/login?error=true")
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestResolver(
                                        new CustomAuthorizationRequestResolver(
                                                clientRegistrationRepository,
                                                "/oauth2/authorization"
                                        )
                                ))
                        .permitAll()
                )
                .logout(auth -> auth.logoutUrl("/auth/logout"))
                .csrf(auth -> auth.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}