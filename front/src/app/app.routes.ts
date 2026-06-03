import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.LoginComponent) },
  { path: 'home', loadComponent: () => import('./pages/home/home').then(m => m.HomeComponent) },
  { path: 'veiculos', loadComponent: () => import('./pages/veiculo/veiculo').then(m => m.VeiculoComponent) },
  { path: 'abastecimentos', loadComponent: () => import('./pages/abastecimento/abastecimento').then(m => m.AbastecimentoPageComponent) },
  { path: 'manutencoes', loadComponent: () => import('./pages/manutencao/manutencao').then(m => m.ManutencaoPageComponent) },
  { path: 'perfil', loadComponent: () => import('./pages/perfil/perfil').then(m => m.PerfilPageComponent) },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
