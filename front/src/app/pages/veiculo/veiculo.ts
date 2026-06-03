import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { VeiculoFormComponent } from '../../components/veiculo-form/veiculo-form';
import { VeiculoListItemComponent } from '../../components/veiculo-list-item/veiculo-list-item';
import { VeiculoService } from '../../services/veiculo.service';
import { AuthService } from '../../services/auth.service';
import { VeiculoResponse, VeiculoCreateRequest } from '../../models/veiculo.model';

@Component({
  selector: 'app-veiculo',
  standalone: true,
  imports: [
    CommonModule,
    SidebarComponent,
    VeiculoFormComponent,
    VeiculoListItemComponent
  ],
  templateUrl: './veiculo.html',
  styleUrls: ['./veiculo.scss']
})
export class VeiculoComponent implements OnInit {
  private veiculoService = inject(VeiculoService);
  private authService = inject(AuthService);
  
  veiculos = signal<VeiculoResponse[]>([]);
  isFormVisible = signal(false);
  isSubmitting = signal(false);
  selectedVeiculo = signal<VeiculoResponse | undefined>(undefined);
  isMobileSidebarOpen = signal(false);

  ngOnInit(): void {
    this.carregarVeiculos();
  }

  carregarVeiculos(): void {
    const userId = this.authService.getUsuarioId();
    if (!userId) return;

    this.veiculoService.listar(userId).subscribe({
      next: (data) => this.veiculos.set(data),
      error: (err) => console.error('Error fetching vehicles', err)
    });
  }

  toggleMobileSidebar(): void {
    this.isMobileSidebarOpen.update(v => !v);
  }

  onNovoVeiculo(): void {
    this.selectedVeiculo.set(undefined);
    this.isFormVisible.set(true);
  }

  onEditar(veiculo: VeiculoResponse): void {
    this.selectedVeiculo.set(veiculo);
    this.isFormVisible.set(true);
  }

  onCancelForm(): void {
    this.isFormVisible.set(false);
    this.selectedVeiculo.set(undefined);
  }

  onDeleteVehicle(veiculoId: number): void {
    const userId = this.authService.getUsuarioId();
    if (!userId) return;

    this.isSubmitting.set(true);
    this.veiculoService.deletar(userId, veiculoId).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.isFormVisible.set(false);
        this.selectedVeiculo.set(undefined);
        this.carregarVeiculos();
      },
      error: (err) => {
        this.isSubmitting.set(false);
        console.error('Error deleting vehicle', err);
      }
    });
  }

  onSave(veiculo: VeiculoCreateRequest): void {
    const userId = this.authService.getUsuarioId();
    if (!userId) return;

    this.isSubmitting.set(true);
    
    const request = this.selectedVeiculo() 
      ? this.veiculoService.atualizar(userId, this.selectedVeiculo()!.id, veiculo)
      : this.veiculoService.criar(userId, veiculo);

    request.subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.isFormVisible.set(false);
        this.selectedVeiculo.set(undefined);
        this.carregarVeiculos();
      },
      error: (err) => {
        this.isSubmitting.set(false);
        console.error('Error saving vehicle', err);
      }
    });
  }
}
