package com.backend_spring.auth.services;

import com.backend_spring.auth.models.Usuario;
import com.backend_spring.auth.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario authenticate(String email, String password) {
        Usuario usuario = usuarioRepository.findByemail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificación de contraseña con -> hasheo de contraseña
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }
}
