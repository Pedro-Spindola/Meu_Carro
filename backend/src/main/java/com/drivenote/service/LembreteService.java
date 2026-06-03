package com.drivenote.service;

import com.drivenote.dto.LembreteCreateRequest;
import com.drivenote.dto.LembreteResponse;
import com.drivenote.dto.LembreteUpdateRequest;
import com.drivenote.entity.Lembrete;
import com.drivenote.entity.Manutencao;
import com.drivenote.entity.PlanoManutencao;
import com.drivenote.entity.Veiculo;
import com.drivenote.enums.StatusLembrete;
import com.drivenote.enums.TipoLembrete;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.LembreteMapper;
import com.drivenote.repository.LembreteRepository;
import com.drivenote.repository.ManutencaoRepository;
import com.drivenote.repository.PlanoManutencaoRepository;
import com.drivenote.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LembreteService {

    private final LembreteRepository repository;
    private final VeiculoRepository veiculoRepository;

    public LembreteResponse criar(LembreteCreateRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        Lembrete lembrete = Lembrete.builder()
                .veiculo(veiculo)
                .titulo(request.titulo())
                .descricao(request.descricao())
                .tipo(request.tipo())
                .dataAlerta(request.dataAlerta())
                .status(StatusLembrete.PENDENTE)
                .build();

        return LembreteMapper.toResponse(repository.save(lembrete));
    }

    public List<LembreteResponse> listar(Long veiculoId) {
        return repository.findAllByVeiculoId(veiculoId)
                .stream().map(LembreteMapper::toResponse)
                .toList();
    }

    public List<LembreteResponse> listarPorUsuario(Long usuarioId) {
        return repository.findAllByVeiculoUsuarioIdOrderByDataAlertaAsc(usuarioId)
                .stream().map(LembreteMapper::toResponse)
                .toList();
    }

    public LembreteResponse concluir(Long lembreteId) {
        Lembrete lembrete = repository.findById(lembreteId)
                .orElseThrow(() -> new ResourceNotFoundException("Lembrete não encontrado"));

        lembrete.setStatus(StatusLembrete.CONCLUIDO);
        return LembreteMapper.toResponse(repository.save(lembrete));
    }

    /**
     * Atualiza lembretes vencidos para ATRASADO.
     * Pode ser chamado em uma rotina agendada depois.
     */
    @Transactional
    public void marcarAtrasados() {
        List<Lembrete> vencidos =
                repository.findAllByStatusAndDataAlertaBefore(StatusLembrete.PENDENTE, LocalDate.now());

        vencidos.forEach(l -> l.setStatus(StatusLembrete.ATRASADO));
        repository.saveAll(vencidos);
    }

    @Transactional
    public LembreteResponse atualizar(Long lembreteId, LembreteUpdateRequest request) {
        Lembrete lembrete = repository.findById(lembreteId)
                .orElseThrow(() -> new ResourceNotFoundException("Lembrete não encontrado"));

        if (lembrete.getStatus() == StatusLembrete.CONCLUIDO) {
            throw new BusinessException("Não é possível editar um lembrete já concluído");
        }

        lembrete.setTitulo(request.titulo());
        lembrete.setDescricao(request.descricao());
        lembrete.setTipo(request.tipo());
        lembrete.setDataAlerta(request.dataAlerta());

        return LembreteMapper.toResponse(repository.save(lembrete));
    }

    @Transactional
    public void deletar(Long lembreteId) {
        Lembrete lembrete = repository.findById(lembreteId)
                .orElseThrow(() -> new ResourceNotFoundException("Lembrete não encontrado"));
        repository.delete(lembrete);
    }
}