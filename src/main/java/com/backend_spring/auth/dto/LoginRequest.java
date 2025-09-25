package com.backend_spring.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
