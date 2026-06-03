package com.drivenote.dto;

import com.drivenote.enums.TipoCombustivel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta do abastecimento.
 */
public record AbastecimentoResponse(
        Long id,
        Long veiculoId,
        LocalDate data,
        TipoCombustivel tipoCombustivel,
        BigDecimal valorTotal,
        BigDecimal litros,
        BigDecimal valorLitro,
        Long quilometragem,
        BigDecimal consumoMedio,
        BigDecimal custoPorKm,
        LocalDateTime createdAt
) {}