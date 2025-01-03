/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.config;

import com.example.middleware.jwt.JwtAuthenticationEntryPoint;
import com.example.middleware.jwt.JwtOncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtOncePerRequestFilter jwtOncePerRequestFilter;

    private static final String[] PUBLIC_URLS = {
            "/",
            "/api/v1/auth/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        /*
         * Enable CORS and disable CSRF
         * Set session management to stateless
         * Set unauthorized requests exception handler
         * Set permissions on endpoints
         * Add JWT filter
         * */
        return httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(PUBLIC_URLS).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
