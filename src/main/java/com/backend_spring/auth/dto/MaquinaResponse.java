package com.backend_spring.auth.dto;

import java.time.LocalDateTime;

public record MaquinaResponse(
        Long idMaquina,
        String nombre,
        String descripcion,
        LocalDateTime fechaCreacion
) {}
