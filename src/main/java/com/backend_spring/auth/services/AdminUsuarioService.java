package com.backend_spring.auth.services;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.models.Usuario;
import com.backend_spring.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // --- CREATE ---
    public UsuarioResponse createUsuario(UsuarioCreateRequest request) {
        Usuario usuario = new Usuario();
        usuario.setIdSede(request.idSede());
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setEmail(request.email());
        usuario.setTelefono(request.telefono());
        usuario.setDni(request.dni());
        usuario.setTipo(request.tipo());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        if (request.estado() != null) {
            usuario.setEstado(request.estado());
        }

        Usuario saved = usuarioRepository.save(usuario);
        return toResponse(saved);
    }

    // --- READ ALL ---
    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // --- READ BY ID ---
    public UsuarioResponse getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return toResponse(usuario);
    }

    // --- UPDATE ---
    public UsuarioResponse updateUsuario(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Actualizamos solo los campos que no son null
        if (request.idSede() != null) usuario.setIdSede(request.idSede());
        if (request.nombre() != null) usuario.setNombre(request.nombre());
        if (request.apellido() != null) usuario.setApellido(request.apellido());
        if (request.telefono() != null) usuario.setTelefono(request.telefono());
        if (request.dni() != null) usuario.setDni(request.dni());
        if (request.estado() != null) usuario.setEstado(request.estado());
        if (request.tipo() != null) usuario.setTipo(request.tipo());
        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        Usuario updated = usuarioRepository.save(usuario);
        return toResponse(updated);
    }

    // --- DELETE ---
    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    // --- HELPER METHOD: ENTITY -> DTO ---
    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getIdSede(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getDni(),
                usuario.getEstado(),
                usuario.getTipo(),
                usuario.getFechaCreacion()
        );
    }
}
