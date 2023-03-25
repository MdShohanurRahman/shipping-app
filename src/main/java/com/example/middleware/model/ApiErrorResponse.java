/**
 * Created By shoh@n
 * Date: 3/25/2023
 */

package com.example.middleware.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ApiErrorResponse {

    private final int status;
    private final String message;
    private List<String> errors;
    private final LocalDateTime timestamp;

    public ApiErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.errors = new ArrayList<>();
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors;
    }
}
