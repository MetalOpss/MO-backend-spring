package com.backend_spring.auth.controllers;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.services.MaquinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/maquinas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class MaquinaController {

    private final MaquinaService maquinaService;

    @PostMapping
    public ResponseEntity<MaquinaResponse> create(@Valid @RequestBody MaquinaCreateRequest request) {
        return ResponseEntity.ok(maquinaService.createMaquina(request));
    }

    @GetMapping
    public ResponseEntity<List<MaquinaResponse>> getAll() {
        return ResponseEntity.ok(maquinaService.getAllMaquinas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaquinaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maquinaService.getMaquinaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaquinaResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MaquinaUpdateRequest request
    ) {
        return ResponseEntity.ok(maquinaService.updateMaquina(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maquinaService.deleteMaquina(id);
        return ResponseEntity.noContent().build();
    }
}
