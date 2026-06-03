# Veiculo UI Components Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create `VeiculoListItemComponent` and `VeiculoFormComponent` for vehicle management.

**Architecture:** Standalone components with multi-file structure. Reactive forms for the form component. CSS variables for styling.

**Tech Stack:** Angular, SCSS, Reactive Forms.

---

### Task 1: Create VeiculoListItemComponent

**Files:**
- Create: `front/src/app/components/veiculo-list-item/veiculo-list-item.ts`
- Create: `front/src/app/components/veiculo-list-item/veiculo-list-item.html`
- Create: `front/src/app/components/veiculo-list-item/veiculo-list-item.scss`

- [ ] **Step 1: Implement the component logic**
```typescript
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VeiculoResponse } from '../../models/veiculo.model';

@Component({
  selector: 'app-veiculo-list-item',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './veiculo-list-item.html',
  styleUrl: './veiculo-list-item.scss'
})
export class VeiculoListItemComponent {
  @Input({ required: true }) veiculo!: VeiculoResponse;
  @Output() editar = new EventEmitter<VeiculoResponse>();

  onEditar(): void {
    this.editar.emit(this.veiculo);
  }
}
```

- [ ] **Step 2: Implement the component template**
```html
<div class="veiculo-item">
  <div class="veiculo-header">
    <h3>{{ veiculo.marca }} {{ veiculo.modelo }}</h3>
    <button class="btn-edit" (click)="onEditar()">Editar</button>
  </div>
  <div class="veiculo-details">
    <div class="detail">
      <span class="label">Ano:</span>
      <span class="value">{{ veiculo.ano }}</span>
    </div>
    <div class="detail">
      <span class="label">Placa:</span>
      <span class="value">{{ veiculo.placa }}</span>
    </div>
    <div class="detail">
      <span class="label">Combustível:</span>
      <span class="value">{{ veiculo.combustivel }}</span>
    </div>
    <div class="detail">
      <span class="label">KM:</span>
      <span class="value">{{ veiculo.quilometragemAtual | number }}</span>
    </div>
  </div>
</div>
```

- [ ] **Step 3: Implement the component styles**
```scss
.veiculo-item {
  background: var(--surface-color);
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-top: 4px solid var(--primary-color);
  margin-bottom: 1rem;
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  }
}

.veiculo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;

  h3 {
    margin: 0;
    font-size: 1.25rem;
    color: var(--text-color);
  }

  .btn-edit {
    background: transparent;
    color: var(--primary-color);
    border: 1px solid var(--primary-color);
    padding: 0.5rem 1rem;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 600;
    transition: all 0.2s ease;

    &:hover {
      background: var(--primary-color);
      color: white;
    }
  }
}

.veiculo-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;

  @media (max-width: 480px) {
    grid-template-columns: 1fr;
  }
}

.detail {
  display: flex;
  flex-direction: column;

  .label {
    font-size: 0.75rem;
    color: var(--text-secondary-color);
    text-transform: uppercase;
    letter-spacing: 0.05em;
  }

  .value {
    font-size: 1rem;
    color: var(--text-color);
    font-weight: 500;
  }
}
```

- [ ] **Step 4: Commit Task 1**
```bash
git add front/src/app/components/veiculo-list-item/
git commit -m "feat: add VeiculoListItemComponent"
```

---

### Task 2: Create VeiculoFormComponent

**Files:**
- Create: `front/src/app/components/veiculo-form/veiculo-form.ts`
- Create: `front/src/app/components/veiculo-form/veiculo-form.html`
- Create: `front/src/app/components/veiculo-form/veiculo-form.scss`

- [ ] **Step 1: Implement the component logic**
```typescript
import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VeiculoResponse, VeiculoCreateRequest, TipoCombustivel } from '../../models/veiculo.model';

@Component({
  selector: 'app-veiculo-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './veiculo-form.html',
  styleUrl: './veiculo-form.scss'
})
export class VeiculoFormComponent implements OnInit {
  @Input() veiculo?: VeiculoResponse;
  @Input() isLoading = signal(false);
  @Output() save = new EventEmitter<VeiculoCreateRequest>();
  @Output() cancel = new EventEmitter<void>();

  form!: FormGroup;
  tiposCombustivel = Object.values(TipoCombustivel);

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      marca: [this.veiculo?.marca || '', [Validators.required]],
      modelo: [this.veiculo?.modelo || '', [Validators.required]],
      ano: [this.veiculo?.ano || new Date().getFullYear(), [Validators.required, Validators.min(1900)]],
      placa: [this.veiculo?.placa || '', [Validators.required, Validators.pattern(/^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$/)]],
      combustivel: [this.veiculo?.combustivel || TipoCombustivel.GASOLINA, [Validators.required]],
      quilometragemAtual: [this.veiculo?.quilometragemAtual || 0, [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.save.emit(this.form.value);
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
```

- [ ] **Step 2: Implement the component template**
```html
<form [formGroup]="form" (ngSubmit)="onSubmit()" class="veiculo-form">
  <div class="form-grid">
    <div class="form-field">
      <label for="marca">Marca</label>
      <input id="marca" type="text" formControlName="marca" placeholder="Ex: Toyota">
      <div class="error" *ngIf="form.get('marca')?.invalid && form.get('marca')?.touched">
        Marca é obrigatória.
      </div>
    </div>

    <div class="form-field">
      <label for="modelo">Modelo</label>
      <input id="modelo" type="text" formControlName="modelo" placeholder="Ex: Corolla">
      <div class="error" *ngIf="form.get('modelo')?.invalid && form.get('modelo')?.touched">
        Modelo é obrigatório.
      </div>
    </div>

    <div class="form-field">
      <label for="ano">Ano</label>
      <input id="ano" type="number" formControlName="ano">
      <div class="error" *ngIf="form.get('ano')?.invalid && form.get('ano')?.touched">
        Ano inválido.
      </div>
    </div>

    <div class="form-field">
      <label for="placa">Placa</label>
      <input id="placa" type="text" formControlName="placa" placeholder="ABC1D23">
      <div class="error" *ngIf="form.get('placa')?.invalid && form.get('placa')?.touched">
        Placa inválida (Formato Mercosul).
      </div>
    </div>

    <div class="form-field">
      <label for="combustivel">Combustível</label>
      <select id="combustivel" formControlName="combustivel">
        <option *ngFor="let tipo of tiposCombustivel" [value]="tipo">{{ tipo }}</option>
      </select>
    </div>

    <div class="form-field">
      <label for="quilometragemAtual">Quilometragem</label>
      <input id="quilometragemAtual" type="number" formControlName="quilometragemAtual">
      <div class="error" *ngIf="form.get('quilometragemAtual')?.invalid && form.get('quilometragemAtual')?.touched">
        KM inválido.
      </div>
    </div>
  </div>

  <div class="form-actions">
    <button type="button" class="btn-cancel" (click)="onCancel()" [disabled]="isLoading()">Cancelar</button>
    <button type="submit" class="btn-save" [disabled]="form.invalid || isLoading()">
      {{ isLoading() ? 'Salvando...' : 'Salvar' }}
    </button>
  </div>
</form>
```

- [ ] **Step 3: Implement the component styles**
```scss
.veiculo-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  background: var(--surface-color);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.25rem;

  @media (max-width: 600px) {
    grid-template-columns: 1fr;
  }
}

.form-field {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  label {
    font-size: 0.9rem;
    font-weight: 600;
    color: var(--text-color);
  }

  input, select {
    padding: 0.75rem;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    background: var(--background-color);
    color: var(--text-color);
    font-size: 1rem;
    transition: border-color 0.2s;

    &:focus {
      outline: none;
      border-color: var(--primary-color);
    }
  }

  .error {
    color: var(--status-delayed-color);
    font-size: 0.8rem;
    margin-top: 0.25rem;
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;

  button {
    padding: 0.75rem 1.5rem;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s;

    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }
  }

  .btn-cancel {
    background: transparent;
    color: var(--text-secondary-color);
    border: 1px solid var(--border-color);

    &:hover:not(:disabled) {
      background: rgba(0, 0, 0, 0.05);
    }
  }

  .btn-save {
    background: var(--primary-color);
    color: white;
    border: none;

    &:hover:not(:disabled) {
      background: darken(#FF9800, 5%);
    }
  }
}
```

- [ ] **Step 4: Commit Task 2**
```bash
git add front/src/app/components/veiculo-form/
git commit -m "feat: add VeiculoFormComponent"
```
