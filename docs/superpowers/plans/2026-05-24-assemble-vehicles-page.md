# Assemble Vehicles Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the Vehicles page (`VeiculoComponent`) to manage vehicle CRUD operations, integrating with `VeiculoService` and existing UI components.

**Architecture:** A standalone Angular component that coordinates a list of vehicles and a creation/edition form. It uses `VeiculoService` for data persistence and shares the standard dashboard layout (sidebar + main content).

**Tech Stack:** Angular, TypeScript, SCSS, RxJS.

---

### Task 1: Create VeiculoComponent (Logic)

**Files:**
- Create: `front/src/app/pages/veiculo/veiculo.ts`

- [ ] **Step 1: Implement the logic**

```typescript
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from '../../components/sidebar/sidebar';
import { VeiculoFormComponent } from '../../components/veiculo-form/veiculo-form';
import { VeiculoListItemComponent } from '../../components/veiculo-list-item/veiculo-list-item';
import { VeiculoService } from '../../services/veiculo.service';
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
  
  veiculos = signal<VeiculoResponse[]>([]);
  isFormVisible = signal(false);
  isSubmitting = signal(false);
  selectedVeiculo = signal<VeiculoResponse | undefined>(undefined);
  isMobileSidebarOpen = signal(false);

  ngOnInit(): void {
    this.carregarVeiculos();
  }

  carregarVeiculos(): void {
    // Using user ID 1 as per instructions
    this.veiculoService.listar(1).subscribe({
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

  onSave(veiculo: VeiculoCreateRequest): void {
    this.isSubmitting.set(true);
    const userId = 1;
    
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
```

### Task 2: Create VeiculoComponent (Template)

**Files:**
- Create: `front/src/app/pages/veiculo/veiculo.html`

- [ ] **Step 1: Implement the template**

```html
<div class="veiculos-layout">
  <app-sidebar [class.show-mobile]="isMobileSidebarOpen()"></app-sidebar>

  <main class="main-content">
    <header class="page-header">
      <div class="header-left">
        <button class="mobile-menu-btn" (click)="toggleMobileSidebar()">
          <span class="material-icons">menu</span>
        </button>
        <div class="welcome-text">
          <h1>Meus Veículos</h1>
          <p>Gerencie sua frota pessoal.</p>
        </div>
      </div>
      <div class="header-right">
        <button class="add-btn" (click)="onNovoVeiculo()">
          <span class="material-icons">add</span>
          <span>Novo Veículo</span>
        </button>
      </div>
    </header>

    <div class="content-container">
      <section class="veiculos-list" *ngIf="!isFormVisible(); else formView">
        <div class="list-grid" *ngIf="veiculos().length > 0; else emptyState">
          <app-veiculo-list-item
            *ngFor="let v of veiculos()"
            [veiculo]="v"
            (editar)="onEditar($event)"
          ></app-veiculo-list-item>
        </div>
        
        <ng-template #emptyState>
          <div class="empty-state">
            <span class="material-icons">directions_car</span>
            <p>Você ainda não possui veículos cadastrados.</p>
            <button class="btn-primary" (click)="onNovoVeiculo()">Cadastrar Primeiro Veículo</button>
          </div>
        </ng-template>
      </section>

      <ng-template #formView>
        <section class="form-container">
          <div class="section-header">
            <h3>{{ selectedVeiculo() ? 'Editar Veículo' : 'Novo Veículo' }}</h3>
          </div>
          <app-veiculo-form
            [veiculo]="selectedVeiculo()"
            [isLoading]="isSubmitting"
            (save)="onSave($event)"
            (cancel)="onCancelForm()"
          ></app-veiculo-form>
        </section>
      </ng-template>
    </div>
  </main>
  
  <div class="mobile-overlay" *ngIf="isMobileSidebarOpen()" (click)="toggleMobileSidebar()"></div>
</div>
```

### Task 3: Create VeiculoComponent (Styles)

**Files:**
- Create: `front/src/app/pages/veiculo/veiculo.scss`

- [ ] **Step 1: Implement the styles (matching home layout)**

```scss
@use "../../../styles/colors" as colors;

.veiculos-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f8fafc;
}

.main-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;

  .header-left {
    display: flex;
    align-items: center;
    gap: 1rem;

    .mobile-menu-btn {
      display: none;
      background: none;
      border: none;
      cursor: pointer;
      color: colors.$text-secondary;
    }

    .welcome-text {
      h1 {
        font-size: 1.875rem;
        font-weight: 700;
        color: colors.$text-primary;
        margin: 0;
      }
      p {
        color: colors.$text-secondary;
        margin: 0.25rem 0 0;
      }
    }
  }

  .add-btn {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.75rem 1.5rem;
    background-color: colors.$primary;
    color: white;
    border: none;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: background-color 0.2s;

    &:hover {
      background-color: colors.$primary-dark;
    }
  }
}

.content-container {
  max-width: 1000px;
}

.list-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 1.5rem;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  text-align: center;

  .material-icons {
    font-size: 4rem;
    color: colors.$text-secondary;
    opacity: 0.3;
    margin-bottom: 1rem;
  }

  p {
    color: colors.$text-secondary;
    font-size: 1.1rem;
    margin-bottom: 1.5rem;
  }
}

.btn-primary {
  padding: 0.75rem 1.5rem;
  background-color: colors.$primary;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
}

.form-container {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  max-width: 600px;

  .section-header {
    margin-bottom: 1.5rem;
    h3 {
      font-size: 1.25rem;
      font-weight: 600;
      margin: 0;
    }
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 1rem;
  }

  .page-header {
    .header-left .mobile-menu-btn {
      display: block;
    }
    
    .header-right .add-btn span:not(.material-icons) {
      display: none;
    }
    
    .add-btn {
      padding: 0.5rem;
    }
  }

  .list-grid {
    grid-template-columns: 1fr;
  }
}
```

### Task 4: Register Route and Update Sidebar

**Files:**
- Modify: `front/src/app/app.routes.ts`
- Modify: `front/src/app/components/sidebar/sidebar.ts`

- [ ] **Step 1: Add route to app.routes.ts**

```typescript
// in front/src/app/app.routes.ts
// Add:
{ path: 'veiculos', loadComponent: () => import('./pages/veiculo/veiculo').then(m => m.VeiculoComponent) },
```

- [ ] **Step 2: Update Sidebar route**

```typescript
// in front/src/app/components/sidebar/sidebar.ts
// Change:
{ label: 'Veículos', icon: 'directions_car', route: '/vehicles' },
// To:
{ label: 'Veículos', icon: 'directions_car', route: '/veiculos' },
```

---

### Task 5: Verify and Commit

- [ ] **Step 1: Syntax check**
Run: `npx tsc -p front/tsconfig.app.json --noEmit`

- [ ] **Step 2: Commit**

```bash
git add front/src/app/pages/veiculo/ front/src/app/app.routes.ts front/src/app/components/sidebar/sidebar.ts
git commit -m "feat: implement vehicles page with CRUD integration"
```
