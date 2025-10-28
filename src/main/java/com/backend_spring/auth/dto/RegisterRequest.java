package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;

public record RegisterRequest(
        Long idSede,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String dni,
        String password,
        String estado,
        Rol tipo
) {}
