import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ManutencaoResponse } from '../../models/manutencao.model';

@Component({
  selector: 'app-manutencao-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './manutencao-table.html',
  styleUrl: './manutencao-table.scss'
})
export class ManutencaoTableComponent {
  manutencoes = input.required<ManutencaoResponse[]>();
}
