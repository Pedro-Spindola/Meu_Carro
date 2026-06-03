package com.drivenote.service;

import com.drivenote.dto.PlanoManutencaoCreateRequest;
import com.drivenote.dto.PlanoManutencaoResponse;
import com.drivenote.dto.PlanoManutencaoUpdateRequest;
import com.drivenote.entity.PlanoManutencao;
import com.drivenote.entity.Veiculo;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.PlanoManutencaoMapper;
import com.drivenote.repository.PlanoManutencaoRepository;
import com.drivenote.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoManutencaoService {

    private final PlanoManutencaoRepository repository;
    private final VeiculoRepository veiculoRepository;

    public PlanoManutencaoResponse criar(PlanoManutencaoCreateRequest request) {

        if (request.intervaloKm() == null && request.intervaloDias() == null) {
            throw new BusinessException("Plano deve ter intervaloKm ou intervaloDias");
        }

        if (repository.existsByVeiculoIdAndTipoAndAtivoTrue(request.veiculoId(), request.tipo())) {
            throw new BusinessException("Já existe plano ativo deste tipo para o veículo");
        }

        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        PlanoManutencao plano = PlanoManutencao.builder()
                .veiculo(veiculo)
                .tipo(request.tipo())
                .descricao(request.descricao())
                .intervaloKm(request.intervaloKm())
                .intervaloDias(request.intervaloDias())
                .ativo(request.ativo() == null ? true : request.ativo())
                .build();

        return PlanoManutencaoMapper.toResponse(repository.save(plano));
    }

    public List<PlanoManutencaoResponse> listar(Long veiculoId) {
        return repository.findAllByVeiculoId(veiculoId)
                .stream().map(PlanoManutencaoMapper::toResponse)
                .toList();
    }

    @Transactional
    public PlanoManutencaoResponse atualizar(Long planoId, PlanoManutencaoUpdateRequest request) {
        PlanoManutencao plano = repository.findById(planoId)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado"));

        if (request.intervaloKm() == null && request.intervaloDias() == null) {
            throw new BusinessException("Plano deve ter intervaloKm ou intervaloDias");
        }

        plano.setDescricao(request.descricao());
        plano.setIntervaloKm(request.intervaloKm());
        plano.setIntervaloDias(request.intervaloDias());
        plano.setAtivo(request.ativo() != null ? request.ativo() : plano.isAtivo());

        return PlanoManutencaoMapper.toResponse(repository.save(plano));
    }

    @Transactional
    public void deletar(Long planoId) {
        PlanoManutencao plano = repository.findById(planoId)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado"));
        repository.delete(plano);
    }
}