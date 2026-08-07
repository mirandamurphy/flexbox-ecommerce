package com.flexbox.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Temporary, minimal security configuration.
 *
 * Adding spring-boot-starter-security without any SecurityFilterChain bean
 * makes Spring Boot fall back to its default behavior: every endpoint,
 * including /api/auth/register and /api/auth/login themselves, requires
 * HTTP Basic Auth. That leaves nothing reachable at all, not just
 * unprotected routes.
 *
 * This permits everything for now so the application is actually usable
 * while the real JWT authentication filter is still being built. Once
 * that filter exists, this should be replaced with one that enforces
 * authentication on protected routes and only permits the genuinely
 * public ones (auth, product/subscription box browsing).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
