package com.drivenote.scheduler;

import com.drivenote.service.LembreteService;
import com.drivenote.service.PlanoManutencaoAlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Rotinas automáticas relacionadas a lembretes.
 */
@Component
@RequiredArgsConstructor
public class LembreteScheduler {

    private final LembreteService lembreteService;
    private final PlanoManutencaoAlertaService alertaService;

    /**
     * Executa diariamente às 00:05.
     * Marca lembretes vencidos como ATRASADO.
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void atualizarLembretesAtrasados() {
        lembreteService.marcarAtrasados();
    }

    /**
     * Executa diariamente às 00:10.
     * Verifica planos com intervalo por dias e gera lembretes automáticos.
     */
    @Scheduled(cron = "0 10 0 * * *")
    public void verificarPlanosPorDias() {
        alertaService.verificarTodosPorDias();
    }
}