package com.backend_spring.auth.dto;

import jakarta.validation.constraints.Size;

public record ServicioUpdateRequest(
        @Size(max = 100) String nombre,
        @Size(max = 100) String descripcion
) {}
