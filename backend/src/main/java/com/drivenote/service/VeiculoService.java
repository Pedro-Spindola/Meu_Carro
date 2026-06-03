package com.drivenote.service;

import com.drivenote.dto.*;
import com.drivenote.entity.Usuario;
import com.drivenote.entity.Veiculo;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.VeiculoMapper;
import com.drivenote.repository.UsuarioRepository;
import com.drivenote.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio de veículos.
 */
@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public VeiculoResponse criar(Long usuarioId, VeiculoCreateRequest request) {
        if (veiculoRepository.existsByPlaca(request.placa())) {
            throw new BusinessException("Placa já cadastrada");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Veiculo veiculo = VeiculoMapper.toEntity(request, usuario);
        return VeiculoMapper.toResponse(veiculoRepository.save(veiculo));
    }

    public VeiculoResponse buscarPorId(Long usuarioId, Long veiculoId) {
        Veiculo veiculo = veiculoRepository.findByIdAndUsuarioId(veiculoId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
        return VeiculoMapper.toResponse(veiculo);
    }

    public List<VeiculoResponse> listar(Long usuarioId) {
        return veiculoRepository.findAllByUsuarioId(usuarioId)
                .stream()
                .map(VeiculoMapper::toResponse)
                .toList();
    }

    public VeiculoResponse atualizar(Long usuarioId, Long veiculoId, VeiculoUpdateRequest request) {
        Veiculo veiculo = veiculoRepository.findByIdAndUsuarioId(veiculoId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        if (!veiculo.getPlaca().equals(request.placa()) &&
                veiculoRepository.existsByPlaca(request.placa())) {
            throw new BusinessException("Placa já cadastrada");
        }

        if (request.quilometragemAtual() < veiculo.getQuilometragemAtual()) {
            throw new BusinessException("Quilometragem não pode diminuir");
        }

        veiculo.setMarca(request.marca());
        veiculo.setModelo(request.modelo());
        veiculo.setAno(request.ano());
        veiculo.setPlaca(request.placa());
        veiculo.setCombustivel(request.combustivel());
        veiculo.setQuilometragemAtual(request.quilometragemAtual());

        return VeiculoMapper.toResponse(veiculoRepository.save(veiculo));
    }

    @Transactional
    public void deletar(Long usuarioId, Long veiculoId) {
        Veiculo veiculo = veiculoRepository.findByIdAndUsuarioId(veiculoId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        veiculoRepository.delete(veiculo);
    }
    }
