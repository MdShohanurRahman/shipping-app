/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) {
        log.error("Unauthorized error: {}", authenticationException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
