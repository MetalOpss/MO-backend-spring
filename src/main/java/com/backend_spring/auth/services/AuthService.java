package com.backend_spring.auth.services;

import com.backend_spring.auth.config.AppConfig;
import com.backend_spring.auth.dto.LoginRequest;
import com.backend_spring.auth.dto.RegisterRequest;
import com.backend_spring.auth.dto.TokenResponse;
import com.backend_spring.auth.models.Rol;
import com.backend_spring.auth.models.Token;
import com.backend_spring.auth.models.Usuario;
import com.backend_spring.auth.repository.TokenRepository;
import com.backend_spring.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    // Registro
    public TokenResponse register(RegisterRequest request){

        var usuario = Usuario.builder()
                .idSede(request.idSede())
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
        saveUsuarioToken(savedUsuario, jwtToken);
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

    // Login
    public TokenResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);
        revokeAllUserTokens(usuario);
        saveUsuarioToken(usuario, jwtToken);
        return new TokenResponse(jwtToken);
    }

    private void  revokeAllUserTokens(final Usuario usuario){
        final List<Token> validUserTokens = tokenRepository
                .findAllByUsuarioIdAndExpiredFalseAndRevokedFalse(usuario.getId());
        if (!validUserTokens.isEmpty()){
            for (final Token token : validUserTokens){
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
