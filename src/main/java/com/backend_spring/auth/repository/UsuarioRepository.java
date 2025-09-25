package com.backend_spring.auth.repository;

import com.backend_spring.auth.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByTelefono(String telefono);
}