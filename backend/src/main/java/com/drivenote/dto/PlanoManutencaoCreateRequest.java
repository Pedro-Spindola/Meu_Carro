package com.drivenote.dto;

import com.drivenote.enums.TipoManutencao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PlanoManutencaoCreateRequest(

        @Schema(example = "1")
        @NotNull
        Long veiculoId,

        @Schema(example = "OLEO_MOTOR")
        @NotNull
        TipoManutencao tipo,

        @Schema(example = "Troca de óleo a cada 10.000 km")
        @Size(max = 150)
        String descricao,

        @Schema(example = "10000")
        Long intervaloKm,

        @Schema(example = "180")
        Integer intervaloDias,

        @Schema(example = "true")
        Boolean ativo
) {}