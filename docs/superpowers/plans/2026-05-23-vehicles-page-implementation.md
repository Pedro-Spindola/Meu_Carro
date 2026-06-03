# Vehicles Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create a "Veículos" page with a list of car containers and a toggleable form for adding/editing vehicles.

**Architecture:** Component-based orchestration. `VeiculoComponent` manages the list and form state. `VeiculoListItemComponent` displays individual car data. `VeiculoFormComponent` handles CRUD inputs.

**Tech Stack:** Angular 21, SCSS, Signals, Reactive Forms.

---

### Task 1: Models and Service

**Files:**
- Create: `front/src/app/models/veiculo.model.ts`
- Create: `front/src/app/services/veiculo.service.ts`

- [ ] **Step 1: Define Vehicle Models**
Create `front/src/app/models/veiculo.model.ts`:
```typescript
export enum TipoCombustivel {
  GASOLINA = 'GASOLINA',
  ETANOL = 'ETANOL',
  DIESEL = 'DIESEL',
  FLEX = 'FLEX'
}

export interface VeiculoResponse {
  id: number;
  marca: string;
  modelo: string;
  ano: number;
  placa: string;
  combustivel: TipoCombustivel;
  quilometragemAtual: number;
}

export interface VeiculoCreateRequest {
  marca: string;
  modelo: string;
  ano: number;
  placa: string;
  combustivel: TipoCombustivel;
  quilometragemAtual: number;
}

export type VeiculoUpdateRequest = VeiculoCreateRequest;
```

- [ ] **Step 2: Implement VeiculoService**
Create `front/src/app/services/veiculo.service.ts`:
```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { VeiculoResponse, VeiculoCreateRequest, VeiculoUpdateRequest } from '../models/veiculo.model';

@Injectable({
  providedIn: 'root'
})
export class VeiculoService {
  private readonly API_URL = (usuarioId: number) => `${environment.apiUrl}/usuarios/${usuarioId}/veiculos`;

  constructor(private http: HttpClient) {}

  listar(usuarioId: number): Observable<VeiculoResponse[]> {
    return this.http.get<VeiculoResponse[]>(this.API_URL(usuarioId));
  }

  criar(usuarioId: number, veiculo: VeiculoCreateRequest): Observable<VeiculoResponse> {
    return this.http.post<VeiculoResponse>(this.API_URL(usuarioId), veiculo);
  }

  atualizar(usuarioId: number, veiculoId: number, veiculo: VeiculoUpdateRequest): Observable<VeiculoResponse> {
    return this.http.put<VeiculoResponse>(`${this.API_URL(usuarioId)}/${veiculoId}`, veiculo);
  }
}
```

- [ ] **Step 3: Commit**
```bash
git add front/src/app/models/veiculo.model.ts front/src/app/services/veiculo.service.ts
git commit -m "feat: add vehicle models and service"
```

---

### Task 2: UI Components (List Item and Form)

**Files:**
- Create: `front/src/app/components/veiculo-list-item/*`
- Create: `front/src/app/components/veiculo-form/*`

- [ ] **Step 1: Create VeiculoListItemComponent**
Modern vertical container with "Editar" button.
Style: Border-top with primary color, rounded corners, shadow.

- [ ] **Step 2: Create VeiculoFormComponent**
Reactive form with validation and `isLoading` signal.
Fields: Marca, Modelo, Ano, Placa, Combustível (Select), KM.

- [ ] **Step 3: Commit**
```bash
git add front/src/app/components/
git commit -m "feat: add vehicle list item and form components"
```

---

### Task 3: Assemble Vehicles Page

**Files:**
- Create: `front/src/app/pages/veiculo/veiculo.ts`
- Create: `front/src/app/pages/veiculo/veiculo.html`
- Create: `front/src/app/pages/veiculo/veiculo.scss`
- Modify: `front/src/app/app.routes.ts`

- [ ] **Step 1: Implement VeiculoComponent**
Logic to toggle form, fetch list (using user ID 1 for now), and handle save success.

- [ ] **Step 2: Register Route**
Add `/veiculos` to `app.routes.ts`.

- [ ] **Step 3: Commit**
```bash
git add front/src/app/pages/veiculo/ front/src/app/app.routes.ts
git commit -m "feat: implement vehicles page with CRUD integration"
```
