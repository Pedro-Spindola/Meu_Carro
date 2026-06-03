package com.drivenote.dto;

import com.drivenote.enums.TipoCombustivel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AbastecimentoCreateRequest(

        @Schema(example = "1")
        @NotNull
        Long veiculoId,

        @Schema(example = "2026-05-17")
        @NotNull
        LocalDate data,

        @Schema(example = "GASOLINA")
        @NotNull
        TipoCombustivel tipoCombustivel,

        @Schema(example = "230.50")
        @NotNull @DecimalMin("0.01")
        BigDecimal valorTotal,

        @Schema(example = "38.0")
        @NotNull @DecimalMin("0.01")
        BigDecimal litros,

        @Schema(example = "6.07")
        BigDecimal valorLitro,

        @Schema(example = "45200")
        @NotNull @Min(0)
        Long quilometragem
) {}