package com.drivenote.controller;

import com.drivenote.dto.LembreteCreateRequest;
import com.drivenote.dto.LembreteResponse;
import com.drivenote.dto.LembreteUpdateRequest;
import com.drivenote.exception.ApiError;
import com.drivenote.service.LembreteService;
import com.drivenote.service.PlanoManutencaoAlertaService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Lembretes", description = "Lembretes automotivos e alertas")
@RestController
@RequestMapping("/api/lembretes")
@RequiredArgsConstructor
public class LembreteController {

    private final LembreteService service;
    private final PlanoManutencaoAlertaService alertaService;

    @Operation(summary = "Criar lembrete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public LembreteResponse criar(@RequestBody @Valid LembreteCreateRequest request) {
        return service.criar(request);
    }

    @Operation(summary = "Listar lembretes por veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/veiculo/{veiculoId}")
    public List<LembreteResponse> listar(@PathVariable Long veiculoId) {
        return service.listar(veiculoId);
    }

    @Operation(summary = "Listar todos os lembretes do usuário")
    @GetMapping("/usuario/{usuarioId}")
    public List<LembreteResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @Operation(summary = "Concluir lembrete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete concluído"),
            @ApiResponse(responseCode = "404", description = "Lembrete não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}/concluir")
    public LembreteResponse concluir(@PathVariable Long id) {
        return service.concluir(id);
    }

    @Operation(
            summary = "Processar planos de manutenção manualmente",
            description = "Força a verificação de todos os planos por dias. Útil para testes."
    )
    @PostMapping("/processar-planos")
    public void processarPlanos() {
        alertaService.verificarTodosPorDias();
    }

    @Operation(summary = "Atualizar lembrete")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lembrete atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Lembrete não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Lembrete já concluído",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{id}")
    public LembreteResponse atualizar(@PathVariable Long id,
                                      @RequestBody @Valid LembreteUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Deletar lembrete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lembrete deletado"),
            @ApiResponse(responseCode = "404", description = "Lembrete não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}