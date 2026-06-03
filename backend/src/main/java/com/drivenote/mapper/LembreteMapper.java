package com.drivenote.mapper;

import com.drivenote.dto.LembreteResponse;
import com.drivenote.entity.Lembrete;

public class LembreteMapper {

    public static LembreteResponse toResponse(Lembrete l) {
        return new LembreteResponse(
                l.getId(),
                l.getVeiculo().getId(),
                l.getVeiculo().getMarca() + " " + l.getVeiculo().getModelo(),
                l.getVeiculo().getPlaca(),
                l.getTitulo(),
                l.getDescricao(),
                l.getTipo(),
                l.getDataAlerta(),
                l.getStatus(),
                l.getCreatedAt()
        );
    }
}