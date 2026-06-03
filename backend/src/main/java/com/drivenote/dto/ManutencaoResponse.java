package com.drivenote.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ManutencaoResponse(
        Long id,
        Long veiculoId,
        Long planoId,
        String descricao,
        LocalDate data,
        Long quilometragem,
        BigDecimal valor,
        LocalDateTime createdAt
) {}