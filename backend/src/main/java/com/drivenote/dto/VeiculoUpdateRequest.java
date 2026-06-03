package com.drivenote.dto;

import com.drivenote.enums.TipoCombustivel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record VeiculoUpdateRequest(

        @Schema(example = "Honda")
        @NotBlank @Size(max = 60)
        String marca,

        @Schema(example = "Civic")
        @NotBlank @Size(max = 60)
        String modelo,

        @Schema(example = "2021")
        @NotNull @Min(1900)
        Integer ano,

        @Schema(example = "ABC1234")
        @NotBlank @Size(max = 10)
        String placa,

        @Schema(example = "FLEX")
        @NotNull
        TipoCombustivel combustivel,

        @Schema(example = "48000")
        @NotNull @Min(0)
        Long quilometragemAtual
) {}