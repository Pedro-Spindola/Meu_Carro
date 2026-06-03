package com.drivenote.dto;

import com.drivenote.enums.TipoManutencao;

import java.time.LocalDateTime;

public record PlanoManutencaoResponse(
        Long id,
        Long veiculoId,
        TipoManutencao tipo,
        String descricao,
        Long intervaloKm,
        Integer intervaloDias,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}