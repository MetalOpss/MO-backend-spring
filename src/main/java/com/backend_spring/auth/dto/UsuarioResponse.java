package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long idUsuario,
        Long idSede,
        String nombre,
        String apellido,
        String email,
        String telefono,
        String dni,
        String estado,
        Rol tipo,
        LocalDateTime fechaCreacion
) {}
