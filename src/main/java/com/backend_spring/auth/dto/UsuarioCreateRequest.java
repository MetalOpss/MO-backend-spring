package com.backend_spring.auth.dto;

import com.backend_spring.auth.models.Rol;
import jakarta.validation.constraints.*;

public record UsuarioCreateRequest(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @Email @NotBlank String email,
        @NotBlank @Size(max = 20) String telefono,
        @NotBlank @Size(max = 15) String dni,
        @NotBlank @Size(min = 6) String password,
        @NotNull Rol tipo
) {}
