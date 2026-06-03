package com.drivenote.mapper;

import com.drivenote.dto.UsuarioCreateRequest;
import com.drivenote.dto.UsuarioResponse;
import com.drivenote.entity.Usuario;

/**
 * Conversor entre entidade e DTO.
 */
public class UsuarioMapper {

    public static Usuario toEntity(UsuarioCreateRequest dto, String senhaCriptografada) {
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(senhaCriptografada)
                .telefone(dto.telefone())
                .build();
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }
}