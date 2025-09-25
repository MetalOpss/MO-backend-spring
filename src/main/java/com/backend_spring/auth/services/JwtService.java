package com.backend_spring.auth.services;

import com.backend_spring.auth.config.JwtProperties;
import com.backend_spring.auth.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.sql.Date;
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

    public String generateToken(final Usuario usuario){
        return buildToken(usuario, getJWTExpiration());
    }

    private String buildToken(final Usuario usuario, final long expirationMs) {
        return Jwts.builder()
                .id(usuario.getId().toString())
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

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
