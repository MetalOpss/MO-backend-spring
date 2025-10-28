package com.backend_spring.auth.controllers;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.services.ServicioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/servicios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ServicioController {

    private final ServicioService servicioService;

    @PostMapping
    public ResponseEntity<ServicioResponse> create(@Valid @RequestBody ServicioCreateRequest request) {
        return ResponseEntity.ok(servicioService.createServicio(request));
    }

    @GetMapping
    public ResponseEntity<List<ServicioResponse>> getAll() {
        return ResponseEntity.ok(servicioService.getAllServicios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.getServicioById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ServicioUpdateRequest request
    ) {
        return ResponseEntity.ok(servicioService.updateServicio(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.deleteServicio(id);
        return ResponseEntity.noContent().build();
    }
}
