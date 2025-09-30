package com.backend_spring.auth.config;

import com.backend_spring.auth.models.Token;
import com.backend_spring.auth.models.Usuario;
import com.backend_spring.auth.repository.TokenRepository;
import com.backend_spring.auth.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwtToken = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwtToken);

            if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            final Optional<Token> tokenOptional = tokenRepository.findByToken(jwtToken);
            if (tokenOptional.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            final Token token = tokenOptional.get();

            if (token.isExpired() || token.isRevoked()) {
                filterChain.doFilter(request, response);
                return;
            }

            final UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            final Optional<Usuario> usuario = usuarioRepository.findByEmail(userDetails.getUsername());

            if (usuario.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            final boolean isTokenValid = jwtService.isTokenValid(jwtToken, usuario.get());
            if (!isTokenValid) {
                filterChain.doFilter(request, response);
                return;
            }

            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {

        }

        filterChain.doFilter(request, response);
    }
}