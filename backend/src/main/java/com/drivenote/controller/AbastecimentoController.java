package com.drivenote.controller;

import com.drivenote.dto.AbastecimentoCreateRequest;
import com.drivenote.dto.AbastecimentoResponse;
import com.drivenote.exception.ApiError;
import com.drivenote.service.AbastecimentoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Abastecimentos", description = "Operações de abastecimento")
@RestController
@RequestMapping("/api/abastecimentos")
@RequiredArgsConstructor
public class AbastecimentoController {

    private final AbastecimentoService service;

    @Operation(
            summary = "Registrar abastecimento",
            description = "Cria um abastecimento, calcula consumo médio e custo por km, "
                    + "e atualiza a quilometragem do veículo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Abastecimento criado"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AbastecimentoResponse criar(@RequestBody @Valid AbastecimentoCreateRequest request) {
        return service.criar(request);
    }

    @Operation(summary = "Listar abastecimentos por veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/veiculo/{veiculoId}")
    public List<AbastecimentoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return service.listarPorVeiculo(veiculoId);
    }

    @Operation(
            summary = "Listar abastecimentos por período",
            description = "Filtra por intervalo de datas (início e fim)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/veiculo/{veiculoId}/periodo")
    public List<AbastecimentoResponse> listarPorPeriodo(@PathVariable Long veiculoId,
                                                        @RequestParam LocalDate inicio,
                                                        @RequestParam LocalDate fim) {
        return service.listarPorPeriodo(veiculoId, inicio, fim);
    }

    @Operation(summary = "Excluir último abastecimento do veículo")
    @DeleteMapping("/veiculo/{veiculoId}/ultimo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirUltimo(@PathVariable Long veiculoId) {
        service.excluirUltimo(veiculoId);
    }
}