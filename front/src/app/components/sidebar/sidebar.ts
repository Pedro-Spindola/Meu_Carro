import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { ThemeService } from '../../services/theme.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss'
})
export class SidebarComponent {
  isCollapsed = signal(false);

  menuItems = [
    { label: 'Home', icon: 'home', route: '/home' },
    { label: 'Veículos', icon: 'directions_car', route: '/veiculos' },
    { label: 'Manutenções', icon: 'build', route: '/manutencoes' },
    { label: 'Abastecimentos', icon: 'local_gas_station', route: '/abastecimentos' },
    { label: 'Perfil', icon: 'person', route: '/perfil' }
  ];

  constructor(
    public themeService: ThemeService,
    private authService: AuthService,
    private router: Router
  ) {}

  toggleSidebar() {
    this.isCollapsed.update(v => !v);
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
      },
      error: () => {
        // Redirect even if backend logout fails
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
      }
    });
  }
}
