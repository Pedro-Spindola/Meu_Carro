package com.drivenote.mapper;

import com.drivenote.dto.AbastecimentoResponse;
import com.drivenote.entity.Abastecimento;

/**
 * Conversor entidade → DTO.
 */
public class AbastecimentoMapper {

    public static AbastecimentoResponse toResponse(Abastecimento a) {
        return new AbastecimentoResponse(
                a.getId(),
                a.getVeiculo().getId(),
                a.getData(),
                a.getTipoCombustivel(),
                a.getValorTotal(),
                a.getLitros(),
                a.getValorLitro(),
                a.getQuilometragem(),
                a.getConsumoMedio(),
                a.getCustoPorKm(),
                a.getCreatedAt()
        );
    }
}