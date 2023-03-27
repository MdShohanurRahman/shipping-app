/**
 * Created By shoh@n
 * Date: 3/27/2023
 */

package com.example.middleware.service;

import com.example.middleware.entity.User;
import com.example.middleware.enums.Role;
import com.example.middleware.exception.ApiException;
import com.example.middleware.jwt.JwtAuthenticationProvider;
import com.example.middleware.jwt.JwtUtils;
import com.example.middleware.model.LoginRequest;
import com.example.middleware.model.RegisterRequest;
import com.example.middleware.model.TokenResponse;
import com.example.middleware.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())){
            throw new ApiException(HttpStatus.CONFLICT, "Email Already exists");
        }
        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(Role.ROLE_VIEWER);
        userRepository.save(user);
        return user;
    }

    @Override
    public TokenResponse getJwtToken(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = jwtAuthenticationProvider.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        return new TokenResponse(token);
    }
}
