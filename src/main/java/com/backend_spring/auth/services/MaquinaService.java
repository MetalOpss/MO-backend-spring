package com.backend_spring.auth.services;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.models.Maquina;
import com.backend_spring.auth.repository.MaquinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquinaService {

    private final MaquinaRepository maquinaRepository;

    // --- CREATE ---
    public MaquinaResponse createMaquina(MaquinaCreateRequest request) {
        Maquina maquina = new Maquina();
        maquina.setNombre(request.nombre());
        maquina.setDescripcion(request.descripcion());

        Maquina saved = maquinaRepository.save(maquina);
        return toResponse(saved);
    }

    // --- READ ALL ---
    public List<MaquinaResponse> getAllMaquinas() {
        return maquinaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // --- READ BY ID ---
    public MaquinaResponse getMaquinaById(Long id) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquina no encontrada"));
        return toResponse(maquina);
    }

    // --- UPDATE ---
    public MaquinaResponse updateMaquina(Long id, MaquinaUpdateRequest request) {
        Maquina maquina = maquinaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maquina no encontrada"));

        if (request.nombre() != null) maquina.setNombre(request.nombre());
        if (request.descripcion() != null) maquina.setDescripcion(request.descripcion());

        Maquina updated = maquinaRepository.save(maquina);
        return toResponse(updated);
    }

    // --- DELETE ---
    public void deleteMaquina(Long id) {
        if (!maquinaRepository.existsById(id)) {
            throw new IllegalArgumentException("Maquina no encontrada");
        }
        maquinaRepository.deleteById(id);
    }

    // --- HELPER METHOD: ENTITY -> DTO ---
    private MaquinaResponse toResponse(Maquina maquina) {
        return new MaquinaResponse(
                maquina.getId_maquina(),
                maquina.getNombre(),
                maquina.getDescripcion(),
                maquina.getFechaCreacion()
        );
    }
}
