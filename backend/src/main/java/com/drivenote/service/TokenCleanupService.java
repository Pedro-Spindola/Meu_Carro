package com.drivenote.service;

import com.drivenote.repository.RefreshTokenRepository;
import com.drivenote.repository.TokenBlacklistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository blacklistRepository;

    // roda a cada 6 horas
    @Scheduled(fixedRate = 21600000)
    @Transactional
    public void cleanup() {
        Instant now = Instant.now();
        refreshTokenRepository.deleteByExpiresAtBefore(now);
        blacklistRepository.deleteByExpiresAtBefore(now);
    }
}