package com.backend_spring.auth.config;

import com.backend_spring.auth.models.Token;
import com.backend_spring.auth.models.Usuario;
import com.backend_spring.auth.repository.TokenRepository;
import com.backend_spring.auth.repository.UsuarioRepository;
import com.backend_spring.auth.security.UsuarioPrincipal;
import com.backend_spring.auth.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("========== JWT FILTER START ==========");
        System.out.println("Path: " + request.getServletPath());

        if (request.getServletPath().startsWith("/auth/") || request.getServletPath().startsWith("/actuator/")) {
            System.out.println("✓ Path permitido sin autenticación");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + (authHeader != null ? "Present" : "NULL"));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("✗ No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwtToken = authHeader.substring(7);
            System.out.println("Token extraído: " + jwtToken.substring(0, Math.min(20, jwtToken.length())) + "...");

            final String userEmail = jwtService.extractUsername(jwtToken);
            System.out.println("Email extraído del token: " + userEmail);

            if (userEmail == null) {
                System.out.println("✗ Email es NULL");
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("✓ Usuario ya autenticado, saltando");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("Buscando token en BD...");
            Token token = tokenRepository.findByToken(jwtToken).orElse(null);

            if (token == null) {
                System.out.println("✗ Token NO encontrado en BD");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("Token encontrado - Expirado: " + token.isExpired() + ", Revocado: " + token.isRevoked());

            if (token.isExpired() || token.isRevoked()) {
                System.out.println("✗ Token expirado o revocado");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("Cargando usuario desde BD...");
            final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) userDetails;

            System.out.println("========== USER INFO ==========");
            System.out.println("Username: " + userDetails.getUsername());
            System.out.println("Authorities: " + userDetails.getAuthorities());
            System.out.println("Enabled: " + userDetails.isEnabled());
            System.out.println("===============================");

            boolean tokenValido = jwtService.isTokenValid(jwtToken, usuarioPrincipal.getUsuario());
            System.out.println("Token válido: " + tokenValido);

            if (!tokenValido) {
                System.out.println("✗ Token NO válido según JwtService");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("✓ Configurando autenticación en SecurityContext");
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            System.out.println("✓ Autenticación configurada exitosamente");
            System.out.println("Authorities en context: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        } catch (Exception e) {
            System.err.println("✗✗✗ ERROR EN JWT FILTER ✗✗✗");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("========== JWT FILTER END ==========\n");
        filterChain.doFilter(request, response);
    }
}
