package com.drivenote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UsuarioCreateRequest(

        @Schema(example = "Maria da Silva")
        @NotBlank @Size(max = 100)
        String nome,

        @Schema(example = "maria@email.com")
        @NotBlank @Email
        String email,

        @Schema(example = "123456")
        @NotBlank
        String senha,

        @Schema(example = "62999999999")
        String telefone
) {}