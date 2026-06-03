package com.drivenote.controller;

import com.drivenote.dto.ManutencaoCreateRequest;
import com.drivenote.dto.ManutencaoResponse;
import com.drivenote.dto.ManutencaoUpdateRequest;
import com.drivenote.exception.ApiError;
import com.drivenote.service.ManutencaoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manutenções", description = "Registro de manutenções realizadas")
@RestController
@RequestMapping("/api/manutencoes")
@RequiredArgsConstructor
public class ManutencaoController {

    private final ManutencaoService service;

    @Operation(
            summary = "Registrar manutenção",
            description = "Registra manutenção do veículo. Atualiza a quilometragem do veículo "
                    + "e permite vincular a um plano preventivo. Valida se a quilometragem "
                    + "não é menor que a atual do veículo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manutenção registrada"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Veículo ou plano não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ManutencaoResponse criar(@RequestBody @Valid ManutencaoCreateRequest request) {
        return service.criar(request);
    }

    @Operation(summary = "Listar manutenções por veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/veiculo/{veiculoId}")
    public List<ManutencaoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return service.listarPorVeiculo(veiculoId);
    }

    @Operation(summary = "Listar manutenções por plano")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Plano não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/plano/{planoId}")
    public List<ManutencaoResponse> listarPorPlano(@PathVariable Long planoId) {
        return service.listarPorPlano(planoId);
    }

    @Operation(summary = "Atualizar manutenção",
            description = "Permite corrigir apenas descrição e valor. " +
                    "Quilometragem e data não podem ser alteradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Manutenção atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Manutenção não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public ManutencaoResponse atualizar(@PathVariable Long id,
                                        @RequestBody @Valid ManutencaoUpdateRequest request) {
        return service.atualizar(id, request);
    }
}