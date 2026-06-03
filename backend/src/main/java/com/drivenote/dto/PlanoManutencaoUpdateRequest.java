package com.drivenote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PlanoManutencaoUpdateRequest(

        @Schema(example = "Troca de óleo 5W30 sintético")
        @Size(max = 150)
        String descricao,

        @Schema(example = "10000")
        Long intervaloKm,

        @Schema(example = "180")
        Integer intervaloDias,

        @Schema(example = "true")
        Boolean ativo
) {}