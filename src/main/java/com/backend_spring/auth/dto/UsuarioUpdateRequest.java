package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        Long idSede,
        String nombre,
        String apellido,
        @Size(max = 20) String telefono,
        @Size(max = 15) String dni,
        @Size(max = 20) String estado,
        Rol tipo,
        @Size(min = 6) String password
) {}
