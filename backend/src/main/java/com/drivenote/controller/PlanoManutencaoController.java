package com.drivenote.controller;

import com.drivenote.dto.PlanoManutencaoCreateRequest;
import com.drivenote.dto.PlanoManutencaoResponse;
import com.drivenote.dto.PlanoManutencaoUpdateRequest;
import com.drivenote.exception.ApiError;
import com.drivenote.service.PlanoManutencaoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Planos de Manutenção", description = "Planos preventivos de manutenção")
@RestController
@RequestMapping("/api/planos-manutencao")
@RequiredArgsConstructor
public class PlanoManutencaoController {

    private final PlanoManutencaoService service;

    @Operation(
            summary = "Criar plano",
            description = "Cria um plano preventivo. Cada veículo pode ter apenas um plano ativo por tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plano criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Plano ativo já existente",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public PlanoManutencaoResponse criar(@RequestBody @Valid PlanoManutencaoCreateRequest request) {
        return service.criar(request);
    }

    @Operation(summary = "Listar planos de um veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/veiculo/{veiculoId}")
    public List<PlanoManutencaoResponse> listar(@PathVariable Long veiculoId) {
        return service.listar(veiculoId);
    }

    @Operation(summary = "Atualizar plano de manutenção",
            description = "Permite atualizar descrição, intervalos e status ativo. O tipo não pode ser alterado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plano atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Plano não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public PlanoManutencaoResponse atualizar(@PathVariable Long id,
                                             @RequestBody @Valid PlanoManutencaoUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Deletar plano de manutenção")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plano deletado"),
            @ApiResponse(responseCode = "404", description = "Plano não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}