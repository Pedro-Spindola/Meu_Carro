package com.drivenote.dto;

import jakarta.validation.constraints.*;

/**
 * DTO usado para atualização de usuário.
 */
public record UsuarioUpdateRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        @Size(max = 20, message = "Telefone inválido")
        String telefone
) {}