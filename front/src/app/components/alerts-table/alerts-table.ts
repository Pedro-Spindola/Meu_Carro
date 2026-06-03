import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LembreteResponse } from '../../models/lembrete.model';

@Component({
  selector: 'app-alerts-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alerts-table.html',
  styleUrl: './alerts-table.scss'
})
export class AlertsTableComponent {
  @Input() alerts: LembreteResponse[] = [];
  @Output() onConcluir = new EventEmitter<number>();
  @Output() onViewDetails = new EventEmitter<LembreteResponse>();

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDENTE': return 'status-pending';
      case 'ATRASADO': return 'status-delayed';
      case 'CONCLUIDO': return 'status-completed';
      default: return '';
    }
  }

  concluir(id: number, event: Event): void {
    event.stopPropagation();
    this.onConcluir.emit(id);
  }

  viewDetails(alert: LembreteResponse): void {
    this.onViewDetails.emit(alert);
  }
}
