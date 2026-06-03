package com.drivenote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoCreateRequest(

        @Schema(example = "1")
        @NotNull
        Long veiculoId,

        @Schema(example = "2")
        Long planoId,

        @Schema(example = "Troca de óleo e filtro")
        @NotBlank @Size(max = 150)
        String descricao,

        @Schema(example = "2026-05-20")
        @NotNull
        LocalDate data,

        @Schema(example = "46500")
        @NotNull @Min(0)
        Long quilometragem,

        @Schema(example = "350.00")
        @NotNull @DecimalMin("0.01")
        BigDecimal valor
) {}