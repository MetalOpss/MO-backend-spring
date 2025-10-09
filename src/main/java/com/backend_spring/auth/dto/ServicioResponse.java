package com.backend_spring.auth.dto;

import java.time.LocalDateTime;

public record ServicioResponse(
        Long idServicio,
        String nombre,
        String descripcion,
        LocalDateTime fechaCreacion,
        Long idMaquina 
) {}
