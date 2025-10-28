package com.backend_spring.auth.services;

import com.backend_spring.auth.config.JwtProperties;
import com.backend_spring.auth.models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    private String getSecret() {
        return jwtProperties.getSecret();
    }

    private long getJWTExpiration() {
        return jwtProperties.getExpirationMs();
    };

    public String extractUsername(final String token) {
        final Claims JwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return JwtToken.getSubject();
    }
    public String generateToken(final Usuario usuario){
        return buildToken(usuario, getJWTExpiration());
    }

    private String buildToken(final Usuario usuario, final long expirationMs) {
        return Jwts.builder()
                .id(usuario.getIdUsuario().toString())
                .claims(Map.of(
                        "nombre", usuario.getNombre(),
                        "rol", usuario.getTipo()
                ))
                .subject(usuario.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(final String token, final Usuario usuario) {
        final String username = extractUsername(token);
        return (username.equals(usuario.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    public Date extractExpiration(final String token) {
        final Claims jwtToken = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return jwtToken.getExpiration();
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
