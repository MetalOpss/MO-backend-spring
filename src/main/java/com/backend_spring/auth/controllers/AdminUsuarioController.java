package com.backend_spring.auth.controllers;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.services.AdminUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class AdminUsuarioController {

    private final AdminUsuarioService adminUsuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioCreateRequest request) {
        return ResponseEntity.ok(adminUsuarioService.createUsuario(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','PLANIFICADOR', 'ATENCION', 'OPERARIO')")
    public ResponseEntity<List<UsuarioResponse>> getAll() {
        return ResponseEntity.ok(adminUsuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adminUsuarioService.getUsuarioById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest request
    ) {
        return ResponseEntity.ok(adminUsuarioService.updateUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminUsuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
