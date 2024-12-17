package org.example.nova.auth.config;


import lombok.RequiredArgsConstructor;
import org.example.nova.auth.entity.MemberRole;
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
                        .requestMatchers("/oauth/admin").hasRole(MemberRole.ADMIN.name())
                        .requestMatchers("/oauth/info").authenticated()
                        .anyRequest().permitAll()
                );

        http
                .formLogin((auth) -> auth.loginPage("/oauth/login")
                        .loginProcessingUrl("/oauth/loginProc")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/oauth")
                        .failureUrl("/oauth")
                        .permitAll());

        http
                .oauth2Login((auth) -> auth.loginPage("/oauth/login")
                        .defaultSuccessUrl("/oauth")
                        .failureUrl("/oauth/login")
                        .permitAll());

        http
                .logout((auth) -> auth
                        .logoutUrl("/oauth/logout"));

        http
                .csrf((auth) -> auth.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){


        return new BCryptPasswordEncoder();
    }
}
