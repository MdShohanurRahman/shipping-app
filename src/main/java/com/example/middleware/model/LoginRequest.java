/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        String email,
        @NotBlank
        String password
) {}
