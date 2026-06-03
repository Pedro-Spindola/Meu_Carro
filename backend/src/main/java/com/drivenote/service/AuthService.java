package com.drivenote.service;

import com.drivenote.dto.AuthRequest;
import com.drivenote.dto.AuthTokens;
import com.drivenote.entity.RefreshToken;
import com.drivenote.entity.TokenBlacklist;
import com.drivenote.entity.Usuario;
import com.drivenote.repository.RefreshTokenRepository;
import com.drivenote.repository.TokenBlacklistRepository;
import com.drivenote.repository.UsuarioRepository;
import com.drivenote.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository blacklistRepository;
    private final UsuarioRepository usuarioRepository;

    public AuthTokens login(AuthRequest request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.email(), request.senha());

        authenticationManager.authenticate(auth);

        String access = jwtService.generateToken(request.email());
        String refresh = jwtService.generateRefreshToken(request.email());

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(refresh)
                        .usuario(usuario)
                        .expiresAt(jwtService.extractExpiration(refresh).toInstant())
                        .revoked(false)
                        .build()
        );

        return new AuthTokens(access, refresh, usuario.getId());
    }

    @Transactional
    public AuthTokens refresh(String refreshToken) {
        String refresh = refreshToken;

        if (!jwtService.isTokenValid(refresh)) {
            throw new BadCredentialsException("Refresh token inválido");
        }

        RefreshToken stored = refreshTokenRepository.findByToken(refresh)
                .orElseThrow(() -> new BadCredentialsException("Refresh token não encontrado"));

        if (stored.isRevoked()) {
            refreshTokenRepository.revokeAllByUsuarioId(stored.getUsuario().getId());
            throw new BadCredentialsException("Refresh token revogado");
        }

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            stored.setRevoked(true);
            refreshTokenRepository.save(stored);
            throw new BadCredentialsException("Refresh token expirado");
        }

        String emailFromToken = jwtService.extractSubject(refresh);
        String emailStored = stored.getUsuario().getEmail();

        if (!emailStored.equals(emailFromToken)) {
            refreshTokenRepository.revokeAllByUsuarioId(stored.getUsuario().getId());
            throw new BadCredentialsException("Refresh token inválido");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        String newAccess = jwtService.generateToken(emailStored);
        String newRefresh = jwtService.generateRefreshToken(emailStored);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(newRefresh)
                        .usuario(stored.getUsuario())
                        .expiresAt(jwtService.extractExpiration(newRefresh).toInstant())
                        .revoked(false)
                        .build()
        );

        return new AuthTokens(newAccess, newRefresh, stored.getUsuario().getId());
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        String jti = jwtService.extractJti(accessToken);
        Date exp = jwtService.extractExpiration(accessToken);

        blacklistRepository.save(
                TokenBlacklist.builder()
                        .jti(jti)
                        .expiresAt(exp.toInstant())
                        .build()
        );

        // logout global simples (Facilitar a vida do Pedro kkk)
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(rt -> refreshTokenRepository.revokeAllByUsuarioId(rt.getUsuario().getId()));
    }
}
