package com.drivenote.security;

import com.drivenote.repository.TokenBlacklistRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth",
            "/api/usuarios",
            "/swagger-ui",
            "/v3/api-docs",
            "/actuator/health"
    );

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenBlacklistRepository blacklistRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean skip = path.startsWith("/api/auth/")
                || ("/api/usuarios".equals(path) && "POST".equals(method))
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator/health");

        System.out.println("=== shouldNotFilter [" + method + " " + path + "] => " + skip);
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("=== JWT FILTER EXECUTANDO: " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("=== Authorization: " + request.getHeader("Authorization"));

        String authHeader = request.getHeader("Authorization");

        // Log 1: ver o que está chegando
        System.out.println(">>> Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>> Header ausente ou sem Bearer");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println(">>> Token extraído: " + token.substring(0, 20) + "...");

        boolean valid = jwtService.isTokenValid(token);
        System.out.println(">>> Token válido? " + valid);

        if (!valid) {
            filterChain.doFilter(request, response);
            return;
        }

        String jti = jwtService.extractJti(token);
        boolean blacklisted = blacklistRepository.existsByJti(jti);
        System.out.println(">>> Token na blacklist? " + blacklisted);

        if (blacklisted) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractSubject(token);
        System.out.println(">>> Email extraído: " + email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}