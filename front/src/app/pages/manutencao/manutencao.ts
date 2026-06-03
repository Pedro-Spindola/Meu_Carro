import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { PlanoCardComponent } from '../../components/plano-card/plano-card';
import { ManutencaoTableComponent } from '../../components/manutencao-table/manutencao-table';
import { PlanoFormComponent } from '../../components/plano-form/plano-form';
import { ManutencaoFormComponent } from '../../components/manutencao-form/manutencao-form';
import { VeiculoService } from '../../services/veiculo.service';
import { PlanoManutencaoService } from '../../services/plano-manutencao.service';
import { ManutencaoService } from '../../services/manutencao.service';
import { AuthService } from '../../services/auth.service';
import { VeiculoResponse } from '../../models/veiculo.model';
import { PlanoManutencaoResponse, TipoManutencao, PlanoManutencaoCreateRequest } from '../../models/plano-manutencao.model';
import { ManutencaoResponse, ManutencaoCreateRequest } from '../../models/manutencao.model';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-manutencao-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    SidebarComponent,
    PlanoCardComponent,
    ManutencaoTableComponent,
    PlanoFormComponent,
    ManutencaoFormComponent
  ],
  templateUrl: './manutencao.html',
  styleUrls: ['./manutencao.scss']
})
export class ManutencaoPageComponent implements OnInit {
  private veiculoService = inject(VeiculoService);
  private planoService = inject(PlanoManutencaoService);
  private manutencaoService = inject(ManutencaoService);
  private authService = inject(AuthService);

  veiculos = signal<VeiculoResponse[]>([]);
  veiculoSelecionadoId = signal<number | null>(null);
  
  planos = signal<PlanoManutencaoResponse[]>([]);
  manutencoes = signal<ManutencaoResponse[]>([]);
  
  veiculoSelecionado = computed(() => 
    this.veiculos().find(v => v.id === this.veiculoSelecionadoId())
  );

  isPlanoFormVisible = signal(false);
  isManutencaoFormVisible = signal(false);
  isSubmitting = signal(false);
  
  selectedPlano = signal<PlanoManutencaoResponse | undefined>(undefined);
  selectedTipo = signal<TipoManutencao | undefined>(undefined);
  
  tiposManutencao = Object.values(TipoManutencao);
  
  isMobileSidebarOpen = signal(false);

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
          this.selecionarVeiculo(vehicleToSelect.id);
        }
      },
      error: (err) => console.error('Error fetching vehicles', err)
    });
  }

  selecionarVeiculo(id: any): void {
    const numericId = typeof id === 'string' ? parseInt(id, 10) : id;
    this.veiculoSelecionadoId.set(numericId);
    this.carregarDadosVeiculo(numericId);
  }

  carregarDadosVeiculo(veiculoId: number): void {
    this.planoService.listarPorVeiculo(veiculoId).subscribe({
      next: (data) => this.planos.set(data),
      error: (err) => console.error('Error fetching plans', err)
    });

    this.manutencaoService.listarPorVeiculo(veiculoId).subscribe({
      next: (data) => this.manutencoes.set(data),
      error: (err) => console.error('Error fetching maintenances', err)
    });
  }

  getPlanoForTipo(tipo: TipoManutencao): PlanoManutencaoResponse | undefined {
    return this.planos().find(p => p.tipo === tipo);
  }

  getUltimaManutencaoForTipo(tipo: TipoManutencao): ManutencaoResponse | undefined {
    return this.manutencoes()
      .filter(m => {
        if (m.planoId) {
          const plano = this.planos().find(p => p.id === m.planoId);
          return plano?.tipo === tipo;
        }
        // Fallback or handle cases where maintenance doesn't have a plan but matches type in description?
        // For now, only match if it has a planId.
        return false;
      })
      .sort((a, b) => new Date(b.data).getTime() - new Date(a.data).getTime())[0];
  }

  onGerenciarPlano(plano: PlanoManutencaoResponse): void {
    this.selectedPlano.set(plano);
    this.selectedTipo.set(plano.tipo);
    this.isPlanoFormVisible.set(true);
  }

  onCriarPlano(tipo: TipoManutencao): void {
    this.selectedPlano.set(undefined);
    this.selectedTipo.set(tipo);
    this.isPlanoFormVisible.set(true);
  }

  onNovoRegistro(plano?: PlanoManutencaoResponse): void {
    this.selectedPlano.set(plano);
    this.isManutencaoFormVisible.set(true);
  }

  onCancelPlanoForm(): void {
    this.isPlanoFormVisible.set(false);
    this.selectedPlano.set(undefined);
    this.selectedTipo.set(undefined);
  }

  onCancelManutencaoForm(): void {
    this.isManutencaoFormVisible.set(false);
    this.selectedPlano.set(undefined);
  }

  onSavePlano(planoReq: PlanoManutencaoCreateRequest): void {
    const veiculoId = this.veiculoSelecionadoId();
    if (!veiculoId) return;

    this.isSubmitting.set(true);
    const request: PlanoManutencaoCreateRequest = {
      ...planoReq,
      veiculoId
    };

    const observable = this.selectedPlano() 
      ? this.planoService.atualizar(this.selectedPlano()!.id, request)
      : this.planoService.criar(request);

    observable.subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.isPlanoFormVisible.set(false);
        this.carregarDadosVeiculo(veiculoId);
      },
      error: (err) => {
        this.isSubmitting.set(false);
        console.error('Error saving plan', err);
      }
    });
  }

  onSaveManutencao(manutencaoReq: ManutencaoCreateRequest): void {
    const veiculoId = this.veiculoSelecionadoId();
    if (!veiculoId) return;

    this.isSubmitting.set(true);
    const request: ManutencaoCreateRequest = {
      ...manutencaoReq,
      veiculoId
    };

    this.manutencaoService.criar(request).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.isManutencaoFormVisible.set(false);
        this.carregarDadosVeiculo(veiculoId);
      },
      error: (err) => {
        this.isSubmitting.set(false);
        console.error('Error saving maintenance', err);
      }
    });
  }

  toggleMobileSidebar(): void {
    this.isMobileSidebarOpen.update(v => !v);
  }
}
