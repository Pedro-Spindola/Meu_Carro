package com.drivenote.service;

import com.drivenote.entity.Lembrete;
import com.drivenote.entity.Manutencao;
import com.drivenote.entity.PlanoManutencao;
import com.drivenote.entity.Veiculo;
import com.drivenote.enums.StatusLembrete;
import com.drivenote.enums.TipoLembrete;
import com.drivenote.repository.LembreteRepository;
import com.drivenote.repository.ManutencaoRepository;
import com.drivenote.repository.PlanoManutencaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanoManutencaoAlertaService {

    private final PlanoManutencaoRepository planoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final LembreteRepository lembreteRepository;

    private static final List<StatusLembrete> STATUS_ATIVOS =
            List.of(StatusLembrete.PENDENTE, StatusLembrete.ATRASADO);

    /**
     * Verifica todos os planos ativos de um veículo e gera lembretes
     * automáticos se necessário. Chamado após cada abastecimento.
     */
    @Transactional
    public void verificarPorVeiculo(Veiculo veiculo) {
        List<PlanoManutencao> planos =
                planoRepository.findAllByVeiculoIdAndAtivoTrue(veiculo.getId());

        for (PlanoManutencao plano : planos) {
            verificarPlano(plano, veiculo);
        }
    }

    /**
     * Verifica todos os planos ativos de todos os veículos por intervalo
     * de dias. Chamado pelo scheduler diário.
     */
    @Transactional
    public void verificarTodosPorDias() {
        List<PlanoManutencao> planos = planoRepository.findAllByAtivoTrue();

        for (PlanoManutencao plano : planos) {
            if (plano.getIntervaloDias() != null) {
                verificarPlano(plano, plano.getVeiculo());
            }
        }
    }

    private void verificarPlano(PlanoManutencao plano, Veiculo veiculo) {
        // Busca a última manutenção executada para este plano
        Manutencao ultimaManutencao = manutencaoRepository
                .findTopByPlanoManutencaoIdOrderByDataDesc(plano.getId())
                .orElse(null);

        boolean alertaPorKm = deveAlertarPorKm(plano, veiculo, ultimaManutencao);
        boolean alertaPorDias = deveAlertarPorDias(plano, ultimaManutencao);

        if (alertaPorKm || alertaPorDias) {
            criarLembreteSeNaoExistir(plano, veiculo, alertaPorKm, alertaPorDias);
        }
    }

    private boolean deveAlertarPorKm(PlanoManutencao plano,
                                     Veiculo veiculo,
                                     Manutencao ultimaManutencao) {
        if (plano.getIntervaloKm() == null) return false;

        long kmBase = ultimaManutencao != null
                ? ultimaManutencao.getQuilometragem()
                : 0L;

        long kmProximaManutencao = kmBase + plano.getIntervaloKm();

        // Alerta quando faltam 500 km ou já passou
        return veiculo.getQuilometragemAtual() >= (kmProximaManutencao - 500);
    }

    private boolean deveAlertarPorDias(PlanoManutencao plano,
                                       Manutencao ultimaManutencao) {
        if (plano.getIntervaloDias() == null) return false;

        LocalDate dataBase = ultimaManutencao != null
                ? ultimaManutencao.getData()
                : LocalDate.now().minusDays(plano.getIntervaloDias()); // nunca feita → já vencida

        LocalDate dataProximaManutencao = dataBase.plusDays(plano.getIntervaloDias());

        // Alerta quando faltam 7 dias ou já passou
        return LocalDate.now().isAfter(dataProximaManutencao.minusDays(7));
    }

    private void criarLembreteSeNaoExistir(PlanoManutencao plano,
                                           Veiculo veiculo,
                                           boolean porKm,
                                           boolean porDias) {
        String titulo = "[Auto] " + plano.getTipo().name().replace("_", " ");

        // Evita duplicar lembrete se já existe um ativo para este plano
        boolean jaExiste = lembreteRepository.existsByVeiculoIdAndTituloAndStatusIn(
                veiculo.getId(), titulo, STATUS_ATIVOS);

        if (jaExiste) {
            log.debug("Lembrete automático já existe para veículo={} plano={}",
                    veiculo.getId(), plano.getTipo());
            return;
        }

        String descricao = montarDescricao(plano, porKm, porDias);

        Lembrete lembrete = Lembrete.builder()
                .veiculo(veiculo)
                .titulo(titulo)
                .descricao(descricao)
                .tipo(TipoLembrete.MANUTENCAO)
                .dataAlerta(LocalDate.now())
                .status(StatusLembrete.PENDENTE)
                .build();

        lembreteRepository.save(lembrete);

        log.info("Lembrete automático criado: veículo={} tipo={} motivo={}",
                veiculo.getId(), plano.getTipo(),
                porKm && porDias ? "KM+DIAS" : porKm ? "KM" : "DIAS");
    }

    private String montarDescricao(PlanoManutencao plano,
                                   boolean porKm,
                                   boolean porDias) {
        StringBuilder sb = new StringBuilder();

        if (plano.getDescricao() != null) {
            sb.append(plano.getDescricao()).append(". ");
        }

        if (porKm && plano.getIntervaloKm() != null) {
            sb.append("Intervalo de ").append(plano.getIntervaloKm()).append(" km atingido. ");
        }

        if (porDias && plano.getIntervaloDias() != null) {
            sb.append("Intervalo de ").append(plano.getIntervaloDias()).append(" dias atingido.");
        }

        return sb.toString().trim();
    }
}