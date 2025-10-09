package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        String nombre,
        String apellido,
        @Size(max = 20) String telefono,
        @Size(max = 15) String dni,
        Rol tipo,
        @Size(min = 6) String password
) {}
