package com.drivenote.service;

import com.drivenote.dto.SenhaUpdateRequest;
import com.drivenote.dto.UsuarioCreateRequest;
import com.drivenote.dto.UsuarioResponse;
import com.drivenote.dto.UsuarioUpdateRequest;
import com.drivenote.entity.Usuario;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.UsuarioMapper;
import com.drivenote.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Regras de negócio relacionadas ao usuário.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo usuário com validações de negócio.
     */
    public UsuarioResponse criar(UsuarioCreateRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        String hash = passwordEncoder.encode(request.senha());
        Usuario usuario = UsuarioMapper.toEntity(request, hash);

        Usuario salvo = repository.save(usuario);
        return UsuarioMapper.toResponse(salvo);
    }

    /**
     * Busca usuário pelo ID.
     */
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return UsuarioMapper.toResponse(usuario);
    }

    /**
     * Atualiza dados básicos do usuário.
     */
    public UsuarioResponse atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setNome(request.nome());
        usuario.setTelefone(request.telefone());

        Usuario salvo = repository.save(usuario);
        return UsuarioMapper.toResponse(salvo);
    }

    /**
     * Altera a senha do usuário.
     */
    @Transactional
    public void alterarSenha(Long id, SenhaUpdateRequest request) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new BusinessException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        repository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        repository.delete(usuario);
        // cascade nos veículos e todos os dados filhos
    }
}