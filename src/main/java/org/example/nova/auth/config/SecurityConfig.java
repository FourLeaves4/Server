package org.example.nova.auth.config;


import lombok.RequiredArgsConstructor;
import org.example.nova.user.entity.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/auth/admin").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/auth/info").authenticated()
                        .anyRequest().permitAll()
                );

        http
                .formLogin((auth) -> auth.loginPage("/auth/login")
                        .loginProcessingUrl("/auth/loginProc")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/auth")
                        .failureUrl("/auth")
                        .permitAll());

        http
                .oauth2Login((auth) -> auth.loginPage("/auth/login")
                        .defaultSuccessUrl("/auth")
                        .failureUrl("/auth/login")
                        .permitAll());

        http
                .logout((auth) -> auth
                        .logoutUrl("/auth/logout"));

        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){


        return new BCryptPasswordEncoder();
    }
}
