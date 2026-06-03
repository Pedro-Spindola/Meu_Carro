# Home Page Structure Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a fully functional and responsive Home page with sidebar navigation, vehicle stats, and real-time data integration.

**Architecture:** Component-based architecture with sub-components for Sidebar, Vehicle Card, Stat Cards, and Alerts Table. Uses Angular Signals for state management and CSS Grid/Flexbox for responsiveness.

**Tech Stack:** Angular 21, SCSS, Signals, RxJS.

---

### Task 1: Global Styles and Models

**Files:**
- Modify: `front/src/styles/_colors.scss`
- Create: `front/src/app/models/dashboard.model.ts`

- [ ] **Step 1: Add status colors to SCSS map**
Update both light and dark maps in `_colors.scss`:
```scss
$colors: (
  light: (
    // ... existing
    status-pending: #9c27b0,
    status-delayed: #f44336,
    status-completed: #4caf50
  ),
  dark: (
    // ... existing
    status-pending: #9c27b0,
    status-delayed: #f44336,
    status-completed: #4caf50
  )
);
```

- [ ] **Step 2: Define Dashboard Models**
Create `front/src/app/models/dashboard.model.ts` based on `DashboardResponse.java`:
```typescript
export interface ProximaManutencao {
  tipo: string;
  kmRestante: number;
  dataPrevista: string;
}

export interface Autonomia {
  autonomiaEstimada: number;
  consumoMedio: number;
}

export interface UltimoAbastecimento {
  data: string;
  litros: number;
  valor: number;
}

export interface GastosMes {
  total: number;
  quantidadeAbastecimentos: number;
}

export interface LembreteResumo {
  id: number;
  titulo: string;
  status: 'PENDENTE' | 'ATRASADO' | 'CONCLUIDO';
  dataAlerta: string;
}

export interface DashboardResponse {
  nomeVeiculo: string;
  modelo: string;
  ano: number;
  placa: string;
  quilometragemAtual: number;
  proximasManutencoes: ProximaManutencao[];
  autonomia: Autonomia;
  ultimoAbastecimento: UltimoAbastecimento;
  gastosMes: GastosMes;
  lembretes: LembreteResumo[];
}
```

- [ ] **Step 3: Commit**
```bash
git add front/src/styles/_colors.scss front/src/app/models/dashboard.model.ts
git commit -m "style: add status colors and dashboard models"
```

---

### Task 2: Auth Interceptor

**Files:**
- Create: `front/src/app/interceptors/auth.interceptor.ts`
- Modify: `front/src/app/app.config.ts`

- [ ] **Step 1: Implement AuthInterceptor**
```typescript
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  if (token) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }
  return next(req);
};
```

- [ ] **Step 2: Register Interceptor in appConfig**
```typescript
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './interceptors/auth.interceptor';
// ...
export const appConfig: ApplicationConfig = {
  providers: [
    // ...
    provideHttpClient(withInterceptors([authInterceptor]))
  ]
};
```

- [ ] **Step 3: Commit**
```bash
git add front/src/app/interceptors/auth.interceptor.ts front/src/app/app.config.ts
git commit -m "feat: add auth interceptor for bearer tokens"
```

---

### Task 3: Dashboard Service

**Files:**
- Create: `front/src/app/services/dashboard.service.ts`

- [ ] **Step 1: Implement DashboardService**
```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DashboardResponse } from '../models/dashboard.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly API_URL = `${environment.apiUrl}/dashboard`;

  constructor(private http: HttpClient) {}

  getDashboard(veiculoId: number): Observable<DashboardResponse> {
    return this.http.get<DashboardResponse>(`${this.API_URL}/veiculo/${veiculoId}`);
  }
}
```

- [ ] **Step 2: Commit**
```bash
git add front/src/app/services/dashboard.service.ts
git commit -m "feat: add dashboard service"
```

---

### Task 3: UI Sub-Components

**Files:**
- Create: `front/src/app/components/vehicle-card/*`
- Create: `front/src/app/components/stat-card/*`
- Create: `front/src/app/components/alerts-table/*`

- [ ] **Step 1: Create VehicleCardComponent**
Standalone component accepting vehicle data via `@input`.
```typescript
// vehicle-card.ts
@Component({
  selector: 'app-vehicle-card',
  standalone: true,
  template: `
    <div class="vehicle-card">
      <div class="info">
        <h3>{{ name }}</h3>
        <p>{{ model }} - {{ year }}</p>
      </div>
      <div class="details">
        <span>Placa: {{ plate }}</span>
        <span>KM: {{ km | number }}</span>
      </div>
    </div>
  `
})
export class VehicleCardComponent {
  @Input() name!: string;
  @Input() model!: string;
  @Input() year!: number;
  @Input() plate!: string;
  @Input() km!: number;
}
```

- [ ] **Step 2: Create StatCardComponent**
Reusable card for metrics.
```typescript
// stat-card.ts
@Component({
  selector: 'app-stat-card',
  standalone: true,
  template: `
    <div class="stat-card">
      <span class="label">{{ label }}</span>
      <span class="value">{{ value }}</span>
    </div>
  `
})
export class StatCardComponent {
  @Input() label!: string;
  @Input() value!: string | number;
}
```

- [ ] **Step 3: Create AlertsTableComponent**
Table with status colors.
```typescript
// alerts-table.html snippet
<tr *ngFor="let alert of alerts">
  <td>{{ alert.titulo }}</td>
  <td [style.color]="getStatusColor(alert.status)">{{ alert.status }}</td>
</tr>

// alerts-table.ts snippet
getStatusColor(status: string) {
  switch(status) {
    case 'PENDENTE': return 'f.c(status-pending)';
    case 'ATRASADO': return 'f.c(status-red)';
    default: return 'f.c(status-green)';
  }
}
```

- [ ] **Step 4: Commit**
```bash
git add front/src/app/components/
git commit -m "feat: add dashboard sub-components"
```

---

### Task 4: Sidebar Component

**Files:**
- Create: `front/src/app/components/sidebar/*`

- [ ] **Step 1: Implement Sidebar with Collapsible logic**
Use a `signal` for `isCollapsed`.
Responsive: Toggle hamburger on mobile, width transition on desktop.

- [ ] **Step 2: Commit**
```bash
git add front/src/app/components/sidebar/
git commit -m "feat: add responsive sidebar component"
```

---

### Task 5: Assemble Home Page

**Files:**
- Modify: `front/src/app/pages/home/home.ts`
- Modify: `front/src/app/pages/home/home.html`
- Modify: `front/src/app/pages/home/home.scss`

- [ ] **Step 1: Integrate data fetching in Home**
Fetch from `DashboardService` using a fixed vehicle ID (or first available) for now.

- [ ] **Step 2: Build Layout**
```html
<div class="home-layout">
  <app-sidebar></app-sidebar>
  <main class="content">
    <!-- Header with Buttons -->
    <!-- Vehicle Card -->
    <!-- Stat Cards Grid -->
    <!-- Alerts Table -->
  </main>
</div>
```

- [ ] **Step 3: Implement Responsiveness**
Grid layout for cards: 1 column on mobile, 4 on desktop.

- [ ] **Step 4: Commit**
```bash
git add front/src/app/pages/home/
git commit -m "feat: assemble home page with real data and responsive layout"
```
