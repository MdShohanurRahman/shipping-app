/**
 * Created By shoh@n
 * Date: 3/26/2023
 */

package com.example.middleware.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email
        @NotBlank
        @Schema(defaultValue = "admin@gmail.com")
        String email,

        @NotBlank
        @Schema(defaultValue = "admin")
        String password
) {}
