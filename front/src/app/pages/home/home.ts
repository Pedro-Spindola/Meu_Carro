import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { VehicleCardComponent } from '../../components/vehicle-card/vehicle-card';
import { StatCardComponent } from '../../components/stat-card/stat-card';
import { AlertsTableComponent } from '../../components/alerts-table/alerts-table';
import { LembreteDetailModalComponent } from '../../components/lembrete-detail-modal/lembrete-detail-modal';
import { RouterLink } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';
import { VeiculoService } from '../../services/veiculo.service';
import { AuthService } from '../../services/auth.service';
import { LembreteService } from '../../services/lembrete.service';
import { UsuarioService } from '../../services/usuario.service';
import { DashboardResponse } from '../../models/dashboard.model';
import { LembreteResponse } from '../../models/lembrete.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    SidebarComponent,
    VehicleCardComponent,
    StatCardComponent,
    AlertsTableComponent,
    LembreteDetailModalComponent
  ],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class HomeComponent implements OnInit {
  dashboardService = inject(DashboardService);
  veiculoService = inject(VeiculoService);
  authService = inject(AuthService);
  lembreteService = inject(LembreteService);
  usuarioService = inject(UsuarioService);
  
  userName = signal<string>('Usuário');
  dashboardData = signal<DashboardResponse | null>(null);
  globalReminders = signal<LembreteResponse[]>([]);
  selectedLembrete = signal<LembreteResponse | null>(null);
  isMobileSidebarOpen = signal(false);

  ngOnInit(): void {
    const usuarioId = this.authService.getUsuarioId();
    
    if (usuarioId) {
      this.loadUsuario(usuarioId);
      this.loadGlobalReminders(usuarioId);
      this.veiculoService.listar(usuarioId).subscribe({
        next: (veiculos) => {
          if (veiculos.length > 0) {
            const preferredIdStr = localStorage.getItem('preferredVehicleId');
            const preferredId = preferredIdStr ? parseInt(preferredIdStr, 10) : null;
            
            const selectedVehicle = veiculos.find(v => v.id === preferredId) || veiculos[0];
            this.loadDashboard(selectedVehicle.id);
          }
        },
        error: (err) => {
          console.error('Error fetching vehicles', err);
        }
      });
    }
  }

  loadUsuario(id: number) {
    this.usuarioService.buscarPorId(id).subscribe({
      next: (user) => this.userName.set(user.nome)
    });
  }

  loadGlobalReminders(usuarioId: number) {
    this.lembreteService.listarPorUsuario(usuarioId).subscribe({
      next: (reminders) => {
        this.globalReminders.set(reminders);
      },
      error: (err) => {
        console.error('Error fetching global reminders', err);
      }
    });
  }

  loadDashboard(veiculoId: number) {
    this.dashboardService.getDashboard(veiculoId).subscribe({
      next: (data) => {
        this.dashboardData.set(data);
      },
      error: (err) => {
        console.error('Error fetching dashboard data', err);
      }
    });
  }

  onConcluirLembrete(id: number) {
    this.lembreteService.concluir(id).subscribe({
      next: () => {
        const usuarioId = this.authService.getUsuarioId();
        if (usuarioId) {
          this.loadGlobalReminders(usuarioId);
        }
      },
      error: (err) => {
        console.error('Error concluding reminder', err);
      }
    });
  }

  onViewLembreteDetails(lembrete: LembreteResponse) {
    this.selectedLembrete.set(lembrete);
  }

  toggleMobileSidebar() {
    this.isMobileSidebarOpen.update(v => !v);
  }

  calculateVehicleStatus(data: DashboardResponse): 'OK' | 'ATENCAO' | 'VENCIDO' | 'PENDENTE' {
    if (!data.proximasManutencoes || data.proximasManutencoes.length === 0) {
      // If there are no maintenance plans, we check if there are reminders
      return this.globalReminders().length > 0 ? 'PENDENTE' : 'OK';
    }

    // Check if any plan is overdue or needs attention
    // Based on the same logic as the Maintenance page cards
    let worstStatus: 'OK' | 'ATENCAO' | 'VENCIDO' | 'PENDENTE' = 'OK';

    // We can also check the actual reminders linked to maintenance
    const hasOverdue = this.globalReminders().some(r => r.status === 'ATRASADO');
    if (hasOverdue) return 'VENCIDO';

    const hasPending = this.globalReminders().some(r => r.status === 'PENDENTE');
    if (hasPending) return 'PENDENTE';

    return worstStatus;
  }
}
