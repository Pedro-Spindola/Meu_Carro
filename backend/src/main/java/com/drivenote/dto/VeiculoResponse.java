package com.drivenote.dto;

import com.drivenote.enums.TipoCombustivel;

import java.time.LocalDateTime;

/**
 * DTO de resposta do veículo.
 */
public record VeiculoResponse(
        Long id,
        String marca,
        String modelo,
        Integer ano,
        String placa,
        TipoCombustivel combustivel,
        Long quilometragemAtual,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}