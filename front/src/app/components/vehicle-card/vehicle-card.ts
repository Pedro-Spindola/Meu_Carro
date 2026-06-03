import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-vehicle-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vehicle-card.html',
  styleUrl: './vehicle-card.scss'
})
export class VehicleCardComponent {
  @Input() name!: string;
  @Input() model!: string;
  @Input() year!: number;
  @Input() plate!: string;
  @Input() km!: number;
  @Input() nextRevision?: string;
  @Input() status: 'OK' | 'ATENCAO' | 'VENCIDO' | 'PENDENTE' = 'OK';
}
