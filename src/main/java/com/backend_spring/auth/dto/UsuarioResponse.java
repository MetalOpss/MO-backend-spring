package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String dni,
        Rol tipo,
        LocalDateTime fechaCreacion
) {}
