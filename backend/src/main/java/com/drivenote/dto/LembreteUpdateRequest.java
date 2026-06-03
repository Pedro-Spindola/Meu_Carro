package com.drivenote.dto;

import com.drivenote.enums.TipoLembrete;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record LembreteUpdateRequest(

        @Schema(example = "IPVA 2026 - Parcela 1")
        @NotBlank @Size(max = 80)
        String titulo,

        @Schema(example = "Vencimento em fevereiro")
        @Size(max = 200)
        String descricao,

        @Schema(example = "IPVA")
        @NotNull
        TipoLembrete tipo,

        @Schema(example = "2026-02-10")
        @NotNull
        LocalDate dataAlerta
) {}