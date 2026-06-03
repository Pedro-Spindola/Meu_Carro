import { Component, computed, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlanoManutencaoResponse, TipoManutencao } from '../../models/plano-manutencao.model';
import { ManutencaoResponse } from '../../models/manutencao.model';

@Component({
  selector: 'app-plano-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './plano-card.html',
  styleUrl: './plano-card.scss'
})
export class PlanoCardComponent {
  tipo = input.required<TipoManutencao>();
  plano = input<PlanoManutencaoResponse | null>(null);
  ultimaManutencao = input<ManutencaoResponse | null>(null);
  quilometragemAtual = input.required<number>();

  gerenciar = output<PlanoManutencaoResponse>();
  criarPlano = output<TipoManutencao>();
  novoRegistro = output<PlanoManutencaoResponse | undefined>();

  statusInfo = computed(() => {
    const p = this.plano();
    const u = this.ultimaManutencao();
    const kmAtual = this.quilometragemAtual();

    if (!p) {
      return { label: 'Sem Plano', class: 'sem-plano', icon: 'add_circle_outline' };
    }

    if (!p.ativo) {
      return { label: 'Inativo', class: 'inativo', icon: 'pause' };
    }

    if (!u) {
      return { label: 'Pendente', class: 'vencido', icon: 'pending_actions' };
    }

    const lastKm = u.quilometragem;
    const lastDate = new Date(u.data);

    let status = 'OK';

    // KM Logic
    if (p.intervaloKm) {
      const kmSinceLast = kmAtual - lastKm;
      if (kmSinceLast >= p.intervaloKm) {
        status = 'VENCIDO';
      } else if (kmSinceLast >= p.intervaloKm * 0.9) {
        if (status !== 'VENCIDO') status = 'ATENCAO';
      }
    }

    // Date Logic
    if (p.intervaloDias && status !== 'VENCIDO') {
      const today = new Date();
      const diffTime = Math.abs(today.getTime() - lastDate.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      
      if (diffDays >= p.intervaloDias) {
        status = 'VENCIDO';
      } else if (p.intervaloDias - diffDays < 15) {
        if (status !== 'VENCIDO') status = 'ATENCAO';
      }
    }

    switch (status) {
      case 'VENCIDO':
        return { label: 'Vencido', class: 'vencido', icon: 'error' };
      case 'ATENCAO':
        return { label: 'Atenção', class: 'atencao', icon: 'warning' };
      default:
        return { label: 'Em dia', class: 'ok', icon: 'check_circle' };
    }
  });

  getTipoLabel(tipo: TipoManutencao): string {
    const labels: Record<TipoManutencao, string> = {
      [TipoManutencao.OLEO_MOTOR]: 'Óleo do Motor',
      [TipoManutencao.FILTRO_OLEO]: 'Filtro de Óleo',
      [TipoManutencao.FILTRO_AR]: 'Filtro de Ar',
      [TipoManutencao.FILTRO_COMBUSTIVEL]: 'Filtro de Combustível',
      [TipoManutencao.PNEU]: 'Pneus',
      [TipoManutencao.FREIO]: 'Freios',
      [TipoManutencao.BATERIA]: 'Bateria',
      [TipoManutencao.ALINHAMENTO]: 'Alinhamento',
      [TipoManutencao.BALANCEAMENTO]: 'Balanceamento',
      [TipoManutencao.SUSPENSAO]: 'Suspensão',
      [TipoManutencao.CORREIA_DENTADA]: 'Correia Dentada',
      [TipoManutencao.REVISAO]: 'Revisão Geral',
      [TipoManutencao.OUTRO]: 'Outros'
    };
    return labels[tipo] || tipo;
  }
}
