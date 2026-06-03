import { Component, OnInit, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { AbastecimentoFormComponent } from '../../components/abastecimento-form/abastecimento-form';
import { AbastecimentoTableComponent } from '../../components/abastecimento-table/abastecimento-table';
import { AbastecimentoService } from '../../services/abastecimento.service';
import { VeiculoService } from '../../services/veiculo.service';
import { AuthService } from '../../services/auth.service';
import { AbastecimentoResponse, AbastecimentoCreateRequest } from '../../models/abastecimento.model';
import { VeiculoResponse } from '../../models/veiculo.model';

@Component({
  selector: 'app-abastecimento-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    SidebarComponent,
    AbastecimentoFormComponent,
    AbastecimentoTableComponent
  ],
  templateUrl: './abastecimento.html',
  styleUrls: ['./abastecimento.scss']
})
export class AbastecimentoPageComponent implements OnInit {
  private abastecimentoService = inject(AbastecimentoService);
  private veiculoService = inject(VeiculoService);
  private authService = inject(AuthService);

  veiculos = signal<VeiculoResponse[]>([]);
  selectedVeiculoId = signal<number | undefined>(undefined);
  abastecimentos = signal<AbastecimentoResponse[]>([]);
  
  isFormVisible = signal(false);
  isSubmitting = signal(false);
  isMobileSidebarOpen = signal(false);

  constructor() {
    // Automatically load refills when selected vehicle changes
    effect(() => {
      const veiculoId = this.selectedVeiculoId();
      if (veiculoId) {
        this.carregarAbastecimentos(veiculoId);
      } else {
        this.abastecimentos.set([]);
      }
    });
  }

  ngOnInit(): void {
    this.carregarVeiculos();
  }

  carregarVeiculos(): void {
    const userId = this.authService.getUsuarioId();
    if (!userId) return;

    this.veiculoService.listar(userId).subscribe({
      next: (data) => {
        this.veiculos.set(data);
        if (data.length > 0) {
          const preferredIdStr = localStorage.getItem('preferredVehicleId');
          const preferredId = preferredIdStr ? parseInt(preferredIdStr, 10) : null;
          
          const vehicleToSelect = data.find(v => v.id === preferredId) || data[0];
          this.selectedVeiculoId.set(vehicleToSelect.id);
        }
      },
      error: (err) => console.error('Error fetching vehicles', err)
    });
  }

  onVeiculoChange(id: any): void {
    const numericId = typeof id === 'string' ? parseInt(id, 10) : id;
    this.selectedVeiculoId.set(numericId);
  }

  carregarAbastecimentos(veiculoId: number): void {
    this.abastecimentoService.listarPorVeiculo(veiculoId).subscribe({
      next: (data) => this.abastecimentos.set(data),
      error: (err) => console.error('Error fetching refills', err)
    });
  }

  toggleMobileSidebar(): void {
    this.isMobileSidebarOpen.update(v => !v);
  }

  onNovoAbastecimento(): void {
    this.isFormVisible.set(true);
  }

  onCancelForm(): void {
    this.isFormVisible.set(false);
  }

  onSave(abastecimento: AbastecimentoCreateRequest): void {
    this.isSubmitting.set(true);
    
    this.abastecimentoService.criar(abastecimento).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.isFormVisible.set(false);
        if (this.selectedVeiculoId() === abastecimento.veiculoId) {
          this.carregarAbastecimentos(abastecimento.veiculoId);
        } else {
          this.selectedVeiculoId.set(abastecimento.veiculoId);
        }
      },
      error: (err) => {
        this.isSubmitting.set(false);
        console.error('Error saving refill', err);
      }
    });
  }
}
