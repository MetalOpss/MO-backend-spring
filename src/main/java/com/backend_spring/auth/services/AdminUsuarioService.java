package com.backend_spring.auth.services;

import com.backend_spring.auth.dto.*;
import com.backend_spring.auth.mappers.UsuarioMapper;
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
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponse createUsuario(UsuarioCreateRequest request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setPassword(passwordEncoder.encode(request.password()));
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(saved);
    }

    public List<UsuarioResponse> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }

    public UsuarioResponse getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }

    public UsuarioResponse updateUsuario(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioMapper.updateEntityFromDto(request, usuario);

        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        Usuario updated = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(updated);
    }

    public void deleteUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
