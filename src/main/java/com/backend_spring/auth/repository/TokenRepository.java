package com.backend_spring.auth.repository;

import com.backend_spring.auth.models.Token;
import com.backend_spring.auth.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    List<Token> findAllByUsuarioAndExpiredFalseAndRevokedFalse(Usuario usuario);
}