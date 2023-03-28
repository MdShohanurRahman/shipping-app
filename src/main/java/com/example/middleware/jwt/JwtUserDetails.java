/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.jwt;

import com.example.middleware.exception.ApiException;
import com.example.middleware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.mode}")
    private String appMode;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if ("admin@gmail.com".equals(email) && "dev".equals(appMode)) {
            return User.withUsername("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMIN")
                    .build();
        }
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email"));
    }
}

