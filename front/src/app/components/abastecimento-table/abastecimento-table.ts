import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbastecimentoResponse } from '../../models/abastecimento.model';

@Component({
  selector: 'app-abastecimento-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './abastecimento-table.html',
  styleUrl: './abastecimento-table.scss'
})
export class AbastecimentoTableComponent {
  @Input() abastecimentos: AbastecimentoResponse[] = [];

  getConsumoClass(consumo: number): string {
    if (!consumo || consumo === 0) return 'neutral';
    if (consumo >= 12) return 'good';
    if (consumo >= 8) return 'average';
    return 'poor';
  }

  getCustoClass(custo: number): string {
    if (!custo || custo === 0) return 'neutral';
    if (custo <= 0.4) return 'good';
    if (custo <= 0.7) return 'average';
    return 'poor';
  }
}
