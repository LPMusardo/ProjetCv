package com.example.projetcv.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Un filtre qui a la charge de récupérer le JWT dans les en-tetes, et de le
 * vérifier si il existe afin de construire le contexte de sécurité Spring
 * Security.
 */

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    final JwtHelper jwtHelper;

	@PostConstruct
    public void init() {
		Logger logger = Logger.getLogger("JwtFilter");
		logger.info("Init JWT filter");
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = jwtHelper.resolveToken(request);

        try {
            if (token != null && jwtHelper.validateToken(token)) {
                Authentication auth = jwtHelper.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (MyJwtException ex) {
            // Cette ligne est très importante pour garantir que le contexte de sécurité est bien supprimé.
            SecurityContextHolder.clearContext();
            response.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }


}
