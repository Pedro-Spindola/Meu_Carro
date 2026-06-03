package com.drivenote.dto;

import java.time.LocalDateTime;

/**
 * DTO de resposta ao cliente.
 */
public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}