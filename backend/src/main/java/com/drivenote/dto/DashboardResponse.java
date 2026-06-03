package com.drivenote.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO agregador para a tela de dashboard.
 */
public record DashboardResponse(
        String nomeVeiculo,
        String modelo,
        Integer ano,
        String placa,
        Long quilometragemAtual,

        List<ProximaManutencao> proximasManutencoes,
        Autonomia autonomia,
        UltimoAbastecimento ultimoAbastecimento,
        GastosMes gastosMes,
        List<LembreteResumo> lembretes
) {

    public record ProximaManutencao(
            String tipo,
            Long kmRestante,
            LocalDate dataPrevista
    ) {}

    public record Autonomia(
            BigDecimal autonomiaEstimada,
            BigDecimal consumoMedio
    ) {}

    public record UltimoAbastecimento(
            LocalDate data,
            BigDecimal litros,
            BigDecimal valor
    ) {}

    public record GastosMes(
            BigDecimal total,
            Long quantidadeAbastecimentos
    ) {}

    public record LembreteResumo(
            Long id,
            String titulo,
            String status,
            LocalDate dataAlerta
    ) {}
}