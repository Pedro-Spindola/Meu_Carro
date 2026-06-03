package com.drivenote.controller;

import com.drivenote.dto.DashboardResponse;
import com.drivenote.exception.ApiError;
import com.drivenote.service.DashboardService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controller do dashboard.
 */
@Tag(name = "Dashboard", description = "Resumo inteligente do veículo")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService service;

    @Operation(
            summary = "Resumo do veículo",
            description = "Retorna um resumo consolidado do veículo incluindo: "
                    + "próximas manutenções (ordenadas por criticidade), "
                    + "autonomia estimada, último abastecimento, gastos do mês "
                    + "e lembretes ativos ordenados por prioridade."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/veiculo/{veiculoId}")
    public DashboardResponse resumo(@PathVariable Long veiculoId) {
        return service.buscarResumo(veiculoId);
    }
}