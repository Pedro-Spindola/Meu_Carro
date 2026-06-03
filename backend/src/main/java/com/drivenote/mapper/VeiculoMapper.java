package com.drivenote.mapper;

import com.drivenote.dto.VeiculoCreateRequest;
import com.drivenote.dto.VeiculoResponse;
import com.drivenote.entity.Usuario;
import com.drivenote.entity.Veiculo;

/**
 * Conversor entre entidade e DTO de veículo.
 */
public class VeiculoMapper {

    public static Veiculo toEntity(VeiculoCreateRequest dto, Usuario usuario) {
        return Veiculo.builder()
                .usuario(usuario)
                .marca(dto.marca())
                .modelo(dto.modelo())
                .ano(dto.ano())
                .placa(dto.placa())
                .combustivel(dto.combustivel())
                .quilometragemAtual(dto.quilometragemAtual())
                .build();
    }

    public static VeiculoResponse toResponse(Veiculo entity) {
        return new VeiculoResponse(
                entity.getId(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAno(),
                entity.getPlaca(),
                entity.getCombustivel(),
                entity.getQuilometragemAtual(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}