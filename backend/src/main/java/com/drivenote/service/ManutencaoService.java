package com.drivenote.service;

import com.drivenote.dto.ManutencaoCreateRequest;
import com.drivenote.dto.ManutencaoResponse;
import com.drivenote.dto.ManutencaoUpdateRequest;
import com.drivenote.entity.Manutencao;
import com.drivenote.entity.PlanoManutencao;
import com.drivenote.entity.Veiculo;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.ManutencaoMapper;
import com.drivenote.repository.ManutencaoRepository;
import com.drivenote.repository.PlanoManutencaoRepository;
import com.drivenote.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final PlanoManutencaoRepository planoRepository;

    public ManutencaoResponse criar(ManutencaoCreateRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        if (request.quilometragem() < veiculo.getQuilometragemAtual()) {
            throw new BusinessException("Quilometragem da manutenção não pode ser menor que a atual do veículo");
        }

        PlanoManutencao plano = null;
        if (request.planoId() != null) {
            plano = planoRepository.findById(request.planoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Plano de manutenção não encontrado"));
        }

        Manutencao manutencao = Manutencao.builder()
                .veiculo(veiculo)
                .planoManutencao(plano)
                .descricao(request.descricao())
                .data(request.data())
                .quilometragem(request.quilometragem())
                .valor(request.valor())
                .build();

        veiculo.setQuilometragemAtual(request.quilometragem());
        veiculoRepository.save(veiculo);

        return ManutencaoMapper.toResponse(manutencaoRepository.save(manutencao));
    }

    public List<ManutencaoResponse> listarPorVeiculo(Long veiculoId) {
        return manutencaoRepository.findAllByVeiculoId(veiculoId)
                .stream().map(ManutencaoMapper::toResponse).toList();
    }

    public List<ManutencaoResponse> listarPorPlano(Long planoId) {
        return manutencaoRepository.findAllByPlanoManutencaoId(planoId)
                .stream().map(ManutencaoMapper::toResponse).toList();


    }

    @Transactional
    public ManutencaoResponse atualizar(Long manutencaoId, ManutencaoUpdateRequest request) {
        Manutencao manutencao = manutencaoRepository.findById(manutencaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Manutenção não encontrada"));

        manutencao.setDescricao(request.descricao());
        manutencao.setValor(request.valor());

        return ManutencaoMapper.toResponse(manutencaoRepository.save(manutencao));
    }
}