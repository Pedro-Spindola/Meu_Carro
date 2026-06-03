package com.drivenote.dto;

import com.drivenote.enums.StatusLembrete;
import com.drivenote.enums.TipoLembrete;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LembreteResponse(
        Long id,
        Long veiculoId,
        String nomeVeiculo,
        String placaVeiculo,
        String titulo,
        String descricao,
        TipoLembrete tipo,
        LocalDate dataAlerta,
        StatusLembrete status,
        LocalDateTime createdAt
) {}