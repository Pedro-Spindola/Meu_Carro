package com.drivenote.dto;

import com.drivenote.enums.TipoLembrete;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record LembreteCreateRequest(

        @Schema(example = "1")
        @NotNull
        Long veiculoId,

        @Schema(example = "IPVA 2026")
        @NotBlank @Size(max = 80)
        String titulo,

        @Schema(example = "Vencimento em janeiro")
        @Size(max = 200)
        String descricao,

        @Schema(
                example = "IPVA",
                description = "Tipo do lembrete. Valores aceitos: MANUTENCAO, DOCUMENTO, SEGURO, " +
                        "IPVA, LICENCIAMENTO, VIAGEM, OUTRO"
        )
        @NotNull
        TipoLembrete tipo,

        @Schema(example = "2026-01-10")
        @NotNull
        LocalDate dataAlerta


) {}