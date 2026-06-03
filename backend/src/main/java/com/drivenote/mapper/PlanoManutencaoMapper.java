package com.drivenote.mapper;

import com.drivenote.dto.PlanoManutencaoResponse;
import com.drivenote.entity.PlanoManutencao;

public class PlanoManutencaoMapper {
    public static PlanoManutencaoResponse toResponse(PlanoManutencao p) {
        return new PlanoManutencaoResponse(
                p.getId(),
                p.getVeiculo().getId(),
                p.getTipo(),
                p.getDescricao(),
                p.getIntervaloKm(),
                p.getIntervaloDias(),
                p.isAtivo(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}