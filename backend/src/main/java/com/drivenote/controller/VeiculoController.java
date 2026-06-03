package com.drivenote.controller;

import com.drivenote.dto.VeiculoCreateRequest;
import com.drivenote.dto.VeiculoResponse;
import com.drivenote.dto.VeiculoUpdateRequest;
import com.drivenote.exception.ApiError;
import com.drivenote.service.VeiculoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Veículos", description = "Gestão de veículos do usuário")
@RestController
@RequestMapping("/api/usuarios/{usuarioId}/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService service;

    @Operation(summary = "Cadastrar veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Placa já cadastrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public VeiculoResponse criar(@PathVariable Long usuarioId,
                                 @RequestBody @Valid VeiculoCreateRequest request) {
        return service.criar(usuarioId, request);
    }

    @Operation(summary = "Buscar veículo por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{veiculoId}")
    public VeiculoResponse buscarPorId(@PathVariable Long usuarioId,
                                       @PathVariable Long veiculoId) {
        return service.buscarPorId(usuarioId, veiculoId);
    }

    @Operation(summary = "Listar veículos do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping
    public List<VeiculoResponse> listar(@PathVariable Long usuarioId) {
        return service.listar(usuarioId);
    }

    @Operation(summary = "Atualizar veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Veículo atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Placa já cadastrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/{veiculoId}")
    public VeiculoResponse atualizar(@PathVariable Long usuarioId,
                                     @PathVariable Long veiculoId,
                                     @RequestBody @Valid VeiculoUpdateRequest request) {
        return service.atualizar(usuarioId, veiculoId, request);
    }

    @Operation(summary = "Deletar veículo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Veículo deletado"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{veiculoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long usuarioId,
                        @PathVariable Long veiculoId) {
        service.deletar(usuarioId, veiculoId);
    }
}