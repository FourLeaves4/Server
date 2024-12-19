package org.example.nova.domain.auth.config;

import lombok.RequiredArgsConstructor;
import org.example.nova.global.verifier.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/admin").hasRole("ADMIN")
                        .requestMatchers("/auth/info").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(auth -> auth
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/loginProc")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/auth", true)
                        .failureUrl("/auth")
                        .permitAll()
                )
                .oauth2Login(auth -> auth
                        .loginPage("/auth/login")
                        .defaultSuccessUrl("/auth", true)
                        .failureUrl("/auth/login?error=true")
                        .failureHandler(customAuthenticationFailureHandler)
                        .authorizationEndpoint(endpoint -> endpoint
                                .authorizationRequestRepository(authorizationRequestRepository())
                        )
                        .permitAll()
                )
                .logout(auth -> auth
                        .logoutUrl("/auth/logout")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                );


        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

}
