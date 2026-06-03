package com.drivenote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ManutencaoUpdateRequest(

        @Schema(example = "Troca de óleo e filtro — revisão completa")
        @NotBlank @Size(max = 150)
        String descricao,

        @Schema(example = "320.00")
        @NotNull @DecimalMin("0.01")
        BigDecimal valor
) {}