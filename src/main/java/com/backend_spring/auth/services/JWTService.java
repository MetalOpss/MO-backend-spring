package com.backend_spring.auth.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JWTService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration")
    private String expiration;

    // Hashear -> SecretKey
    private Key getSigningKey() {
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaimsJWT(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
