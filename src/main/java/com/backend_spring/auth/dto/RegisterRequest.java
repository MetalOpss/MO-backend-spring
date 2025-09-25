package com.backend_spring.auth.dto;

public record RegisterRequest(
        String nombre,
        String apellido,
        String email,
        String telefono,
        String dni,
        String password,
        String tipo
) {}
