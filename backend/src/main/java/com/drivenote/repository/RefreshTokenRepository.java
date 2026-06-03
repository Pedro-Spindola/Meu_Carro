package com.drivenote.repository;

import com.drivenote.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    long deleteByExpiresAtBefore(Instant now);

    @Modifying
    @Query("update RefreshToken rt set rt.revoked = true where rt.usuario.id = :usuarioId")
    int revokeAllByUsuarioId(Long usuarioId);
}