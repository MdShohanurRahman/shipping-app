/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.service;

import com.example.middleware.entity.User;
import com.example.middleware.model.LoginRequest;
import com.example.middleware.model.RegisterRequest;
import com.example.middleware.model.TokenResponse;

import java.util.Optional;

public interface AuthService {

    Optional<User> findUserByEmail(String email);

    User register(RegisterRequest request);

    TokenResponse getJwtToken(LoginRequest request);
}
