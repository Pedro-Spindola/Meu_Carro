import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import { VeiculoService } from '../../services/veiculo.service';
import { ThemeService } from '../../services/theme.service';
import { LembreteService } from '../../services/lembrete.service';
import { VeiculoResponse } from '../../models/veiculo.model';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, SidebarComponent],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.scss']
})
export class PerfilPageComponent implements OnInit {
  private fb = inject(FormBuilder);
  private usuarioService = inject(UsuarioService);
  private authService = inject(AuthService);
  private veiculoService = inject(VeiculoService);
  private lembreteService = inject(LembreteService);
  public themeService = inject(ThemeService);

  perfilForm: FormGroup;
  senhaForm: FormGroup;
  
  veiculos = signal<VeiculoResponse[]>([]);
  usuarioId: number | null = null;
  preferredVehicleId = signal<string>(localStorage.getItem('preferredVehicleId') || '');
  
  isMobileSidebarOpen = signal(false);
  isLoading = signal(false);
  message = signal<{ type: 'success' | 'error', text: string } | null>(null);

  constructor() {
    this.perfilForm = this.fb.group({
      nome: ['', Validators.required],
      telefone: ['', Validators.required],
      email: [{ value: '', disabled: true }]
    });

    this.senhaForm = this.fb.group({
      senhaAtual: ['', Validators.required],
      novaSenha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.usuarioId = this.authService.getUsuarioId();
    if (this.usuarioId) {
      this.loadUsuario();
      this.loadVeiculos();
    }
  }

  private loadUsuario() {
    if (!this.usuarioId) return;
    this.usuarioService.buscarPorId(this.usuarioId).subscribe({
      next: (user) => {
        this.perfilForm.patchValue({
          nome: user.nome,
          telefone: user.telefone,
          email: user.email
        });
      }
    });
  }

  private loadVeiculos() {
    if (!this.usuarioId) return;
    this.veiculoService.listar(this.usuarioId).subscribe({
      next: (veiculos) => this.veiculos.set(veiculos)
    });
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('novaSenha')?.value === g.get('confirmarSenha')?.value
      ? null : { mismatch: true };
  }

  onSavePerfil() {
    if (this.perfilForm.invalid || !this.usuarioId) return;
    
    this.isLoading.set(true);
    this.usuarioService.atualizar(this.usuarioId, this.perfilForm.getRawValue()).subscribe({
      next: () => {
        this.showMessage('success', 'Perfil atualizado com sucesso!');
        this.isLoading.set(false);
      },
      error: () => {
        this.showMessage('error', 'Erro ao atualizar perfil.');
        this.isLoading.set(false);
      }
    });
  }

  onSaveSenha() {
    if (this.senhaForm.invalid || !this.usuarioId) return;

    this.isLoading.set(true);
    const { senhaAtual, novaSenha } = this.senhaForm.value;
    this.usuarioService.alterarSenha(this.usuarioId, { senhaAtual, novaSenha }).subscribe({
      next: () => {
        this.showMessage('success', 'Senha alterada com sucesso!');
        this.senhaForm.reset();
        this.isLoading.set(false);
      },
      error: (err) => {
        const errorMsg = err.error?.message || 'Erro ao alterar senha. Verifique sua senha atual.';
        this.showMessage('error', errorMsg);
        this.isLoading.set(false);
      }
    });
  }

  onPreferredVehicleChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    const id = select.value;
    if (id) {
      localStorage.setItem('preferredVehicleId', id);
      this.preferredVehicleId.set(id);
      this.showMessage('success', 'Veículo preferido atualizado!');
    } else {
      localStorage.removeItem('preferredVehicleId');
      this.preferredVehicleId.set('');
    }
  }

  toggleTheme() {
    this.themeService.toggleTheme();
  }

  toggleMobileSidebar() {
    this.isMobileSidebarOpen.update(v => !v);
  }

  onSimulateScheduler() {
    this.isLoading.set(true);
    this.lembreteService.processarVencidos().subscribe({
      next: () => {
        this.showMessage('success', 'Simulação de lembretes concluída!');
        this.isLoading.set(false);
      },
      error: () => {
        this.showMessage('error', 'Erro ao processar lembretes.');
        this.isLoading.set(false);
      }
    });
  }

  private showMessage(type: 'success' | 'error', text: string) {
    this.message.set({ type, text });
    setTimeout(() => this.message.set(null), 3000);
  }
}
