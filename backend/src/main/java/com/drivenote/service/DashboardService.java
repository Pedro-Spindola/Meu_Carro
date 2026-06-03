package com.drivenote.service;

import com.drivenote.dto.DashboardResponse;
import com.drivenote.entity.*;
import com.drivenote.enums.StatusLembrete;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Serviço agregador para o dashboard.
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VeiculoRepository veiculoRepository;
    private final AbastecimentoRepository abastecimentoRepository;
    private final AbastecimentoCustomRepository abastecimentoCustomRepository;
    private final PlanoManutencaoRepository planoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final LembreteRepository lembreteRepository;

    public DashboardResponse buscarResumo(Long veiculoId) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));

        List<DashboardResponse.ProximaManutencao> proximasManutencoes = calcularProximasManutencoes(veiculo);
        DashboardResponse.Autonomia autonomia = calcularAutonomia(veiculo);
        DashboardResponse.UltimoAbastecimento ultimoAbastecimento = calcularUltimoAbastecimento(veiculo);
        DashboardResponse.GastosMes gastosMes = calcularGastosMes(veiculo);
        List<DashboardResponse.LembreteResumo> lembretes = buscarLembretes(veiculo);

        return new DashboardResponse(
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getPlaca(),
                veiculo.getQuilometragemAtual(),
                proximasManutencoes,
                autonomia,
                ultimoAbastecimento,
                gastosMes,
                lembretes
        );
    }

    /**
     * Calcula lista de próximas manutenções ordenadas por criticidade.
     */
    private List<DashboardResponse.ProximaManutencao> calcularProximasManutencoes(Veiculo veiculo) {
        return planoRepository.findAllByVeiculoId(veiculo.getId())
                .stream()
                .filter(PlanoManutencao::isAtivo)
                .map(plano -> {

                    var ultima = manutencaoRepository
                            .findTopByPlanoManutencaoIdOrderByDataDesc(plano.getId());

                    Long kmRestante = null;
                    LocalDate dataPrevista = null;

                    if (plano.getIntervaloKm() != null) {
                        long baseKm = ultima.map(Manutencao::getQuilometragem)
                                .orElse(veiculo.getQuilometragemAtual());
                        long proximaKm = baseKm + plano.getIntervaloKm();
                        kmRestante = proximaKm - veiculo.getQuilometragemAtual();
                        if (kmRestante < 0) kmRestante = 0L;
                    }

                    if (plano.getIntervaloDias() != null) {
                        LocalDate baseData = ultima.map(Manutencao::getData)
                                .orElse(LocalDate.now());
                        dataPrevista = baseData.plusDays(plano.getIntervaloDias());
                    }

                    return new DashboardResponse.ProximaManutencao(
                            plano.getTipo().name(),
                            kmRestante,
                            dataPrevista
                    );
                })
                .sorted(Comparator
                        .comparing((DashboardResponse.ProximaManutencao p) -> p.kmRestante() == null ? Long.MAX_VALUE : p.kmRestante())
                        .thenComparing(p -> p.dataPrevista() == null ? LocalDate.MAX : p.dataPrevista()))
                .toList();
    }

    private DashboardResponse.Autonomia calcularAutonomia(Veiculo veiculo) {
        return abastecimentoRepository
                .findTopByVeiculoIdOrderByQuilometragemDesc(veiculo.getId())
                .map(ultimo -> new DashboardResponse.Autonomia(
                        ultimo.getConsumoMedio().multiply(ultimo.getLitros()),
                        ultimo.getConsumoMedio()
                ))
                .orElse(new DashboardResponse.Autonomia(BigDecimal.ZERO, BigDecimal.ZERO));
    }

    private DashboardResponse.UltimoAbastecimento calcularUltimoAbastecimento(Veiculo veiculo) {
        return abastecimentoRepository
                .findTopByVeiculoIdOrderByQuilometragemDesc(veiculo.getId())
                .map(ultimo -> new DashboardResponse.UltimoAbastecimento(
                        ultimo.getData(),
                        ultimo.getLitros(),
                        ultimo.getValorTotal()
                ))
                .orElse(null);
    }

    private DashboardResponse.GastosMes calcularGastosMes(Veiculo veiculo) {
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fim = LocalDate.now();

        List<Abastecimento> abastecimentos =
                abastecimentoCustomRepository.findAllByVeiculoIdAndDataBetween(veiculo.getId(), inicio, fim);

        BigDecimal total = abastecimentos.stream()
                .map(Abastecimento::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardResponse.GastosMes(total, (long) abastecimentos.size());
    }

    private List<DashboardResponse.LembreteResumo> buscarLembretes(Veiculo veiculo) {
        return lembreteRepository.findAllByVeiculoId(veiculo.getId())
                .stream()
                .sorted(Comparator
                        .comparing((Lembrete l) -> l.getStatus() == StatusLembrete.ATRASADO ? 0 : 1)
                        .thenComparing(Lembrete::getDataAlerta))
                .map(l -> new DashboardResponse.LembreteResumo(
                        l.getId(),
                        l.getTitulo(),
                        l.getStatus().name(),
                        l.getDataAlerta()
                ))
                .toList();
    }
}