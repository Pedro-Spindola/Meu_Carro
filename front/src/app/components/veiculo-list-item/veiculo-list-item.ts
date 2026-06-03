import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VeiculoResponse } from '../../models/veiculo.model';

@Component({
  selector: 'app-veiculo-list-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './veiculo-list-item.html',
  styleUrl: './veiculo-list-item.scss'
})
export class VeiculoListItemComponent {
  @Input({ required: true }) veiculo!: VeiculoResponse;
  @Output() editar = new EventEmitter<VeiculoResponse>();

  onEditar(): void {
    this.editar.emit(this.veiculo);
  }
}
