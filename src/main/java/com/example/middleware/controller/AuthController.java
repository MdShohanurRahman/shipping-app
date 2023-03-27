/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.controller;

import com.example.middleware.entity.User;
import com.example.middleware.model.LoginRequest;
import com.example.middleware.model.RegisterRequest;
import com.example.middleware.model.TokenResponse;
import com.example.middleware.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> token(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.getJwtToken(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(user.getUuid());
    }
}
