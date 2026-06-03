package com.drivenote.mapper;

import com.drivenote.dto.ManutencaoResponse;
import com.drivenote.entity.Manutencao;

public class ManutencaoMapper {
    public static ManutencaoResponse toResponse(Manutencao m) {
        return new ManutencaoResponse(
                m.getId(),
                m.getVeiculo().getId(),
                m.getPlanoManutencao() != null ? m.getPlanoManutencao().getId() : null,
                m.getDescricao(),
                m.getData(),
                m.getQuilometragem(),
                m.getValor(),
                m.getCreatedAt()
        );
    }
}