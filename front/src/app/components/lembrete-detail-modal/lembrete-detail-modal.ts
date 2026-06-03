import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LembreteResponse } from '../../models/lembrete.model';

@Component({
  selector: 'app-lembrete-detail-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lembrete-detail-modal.html',
  styleUrl: './lembrete-detail-modal.scss'
})
export class LembreteDetailModalComponent {
  @Input({ required: true }) lembrete!: LembreteResponse;
  @Output() close = new EventEmitter<void>();

  onClose(): void {
    this.close.emit();
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDENTE': return 'status-pending';
      case 'ATRASADO': return 'status-delayed';
      case 'CONCLUIDO': return 'status-completed';
      default: return '';
    }
  }

  getTipoIcon(tipo: string): string {
    switch (tipo) {
      case 'MANUTENCAO': return 'build';
      case 'DOCUMENTO': return 'description';
      case 'SEGURO': return 'verified_user';
      case 'IPVA': return 'payments';
      case 'LICENCIAMENTO': return 'assignment';
      case 'VIAGEM': return 'flight';
      default: return 'notifications';
    }
  }
}
