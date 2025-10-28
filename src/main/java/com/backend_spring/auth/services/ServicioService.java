package com.backend_spring.auth.services;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.models.Maquina;
import com.backend_spring.auth.models.Servicio;
import com.backend_spring.auth.repository.MaquinaRepository;
import com.backend_spring.auth.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final MaquinaRepository maquinaRepository;

    // --- CREATE ---
    public ServicioResponse createServicio(ServicioCreateRequest request) {
        Maquina maquina = maquinaRepository.findById(request.idMaquina())
                .orElseThrow(() -> new IllegalArgumentException("Maquina no encontrada"));

        Servicio servicio = new Servicio();
        servicio.setNombre(request.nombre());
        servicio.setDescripcion(request.descripcion());
        servicio.setMaquina(maquina);

        Servicio saved = servicioRepository.save(servicio);
        return toResponse(saved);
    }

    // --- READ ALL ---
    public List<ServicioResponse> getAllServicios() {
        return servicioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // --- READ BY ID ---
    public ServicioResponse getServicioById(Long id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));
        return toResponse(servicio);
    }

    // --- UPDATE ---
    public ServicioResponse updateServicio(Long id, ServicioUpdateRequest request) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));

        if (request.nombre() != null) servicio.setNombre(request.nombre());
        if (request.descripcion() != null) servicio.setDescripcion(request.descripcion());
        if (request.idMaquina() != null) {
            Maquina maquina = maquinaRepository.findById(request.idMaquina())
                    .orElseThrow(() -> new IllegalArgumentException("Maquina no encontrada"));
            servicio.setMaquina(maquina);
        }

        Servicio updated = servicioRepository.save(servicio);
        return toResponse(updated);
    }

    // --- DELETE ---
    public void deleteServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Servicio no encontrado");
        }
        servicioRepository.deleteById(id);
    }

    // --- HELPER METHOD: ENTITY -> DTO ---
    private ServicioResponse toResponse(Servicio servicio) {
        return new ServicioResponse(
                servicio.getId_servicio(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getFechaCreacion(),
                servicio.getMaquina() != null ? servicio.getMaquina().getId_maquina() : null
        );
    }
}
