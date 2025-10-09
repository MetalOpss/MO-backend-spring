package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;
import jakarta.validation.constraints.*;

public record UsuarioUpdateRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Size(max = 20) String telefono,
        @NotBlank @Size(max = 15) String dni,
        @NotNull Rol tipo,
        @Size(min = 6) String password
) {}
