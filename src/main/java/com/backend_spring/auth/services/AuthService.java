package com.backend_spring.auth.services;

import com.backend_spring.auth.config.AppConfig;
import com.backend_spring.auth.dto.RegisterRequest;
import com.backend_spring.auth.dto.TokenResponse;
import com.backend_spring.auth.models.Rol;
import com.backend_spring.auth.models.Token;
import com.backend_spring.auth.models.Usuario;
import com.backend_spring.auth.repository.TokenRepository;
import com.backend_spring.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse register(RegisterRequest request){

        var usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .email(request.email())
                .telefono(request.telefono())
                .dni(request.dni())
                .password(passwordEncoder.encode(request.password()))
                .tipo(request.tipo())
                .build();

        var savedUsuario = usuarioRepository.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);

        return new TokenResponse(jwtToken);
    }

    private void saveUsuarioToken(Usuario usuario, String jwtToken) {
        var token = Token.builder()
                .usuario(usuario)
                .token(jwtToken)
                .tokenType(Token.TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
