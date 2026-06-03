# Abastecimento Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a complete Fuel Refills (Abastecimento) management page allowing users to list, filter by vehicle, and register new refills with performance metrics.

**Architecture:** Following the project's Angular patterns: models for data structures, a dedicated service for API communication, and standalone components for the list, form, and main page.

**Tech Stack:** Angular (TypeScript), RxJS, Tailwind/Sass (consistent with project style).

---

### Task 1: Models and Service

**Files:**
- Create: `front/src/app/models/abastecimento.model.ts`
- Create: `front/src/app/services/abastecimento.service.ts`

- [ ] **Step 1: Create the Abastecimento model**

```typescript
import { TipoCombustivel } from './veiculo.model';

export interface AbastecimentoResponse {
  id: number;
  veiculoId: number;
  data: string;
  tipoCombustivel: TipoCombustivel;
  valorTotal: number;
  litros: number;
  valorLitro: number;
  quilometragem: number;
  consumoMedio: number;
  custoPorKm: number;
  createdAt: string;
}

export interface AbastecimentoCreateRequest {
  veiculoId: number;
  data: string;
  tipoCombustivel: TipoCombustivel;
  valorTotal: number;
  litros: number;
  valorLitro?: number;
  quilometragem: number;
}
```

- [ ] **Step 2: Create the Abastecimento service**

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AbastecimentoResponse, AbastecimentoCreateRequest } from '../models/abastecimento.model';

@Injectable({
  providedIn: 'root'
})
export class AbastecimentoService {
  private readonly API_URL = `${environment.apiUrl}/abastecimentos`;

  constructor(private http: HttpClient) {}

  listarPorVeiculo(veiculoId: number): Observable<AbastecimentoResponse[]> {
    return this.http.get<AbastecimentoResponse[]>(`${this.API_URL}/veiculo/${veiculoId}`);
  }

  criar(request: AbastecimentoCreateRequest): Observable<AbastecimentoResponse> {
    return this.http.post<AbastecimentoResponse>(this.API_URL, request);
  }
}
```

- [ ] **Step 3: Commit**

```bash
git add front/src/app/models/abastecimento.model.ts front/src/app/services/abastecimento.service.ts
git commit -m "feat: add abastecimento model and service"
```

### Task 2: UI Components (Form and Table)

**Files:**
- Create: `front/src/app/components/abastecimento-form/` (ts, html, scss)
- Create: `front/src/app/components/abastecimento-table/` (ts, html, scss)

- [ ] **Step 1: Create AbastecimentoFormComponent**
Implement a reactive form with validations for `data`, `tipoCombustivel`, `valorTotal`, `litros`, and `quilometragem`.

- [ ] **Step 2: Create AbastecimentoTableComponent**
Implement a table displaying the list of refills, with special attention to `consumoMedio` and `custoPorKm`.

- [ ] **Step 3: Commit**

```bash
git add front/src/app/components/abastecimento-form/ front/src/app/components/abastecimento-table/
git commit -m "feat: add abastecimento form and table components"
```

### Task 3: Main Page and Routing

**Files:**
- Create: `front/src/app/pages/abastecimento/` (ts, html, scss)
- Modify: `front/src/app/app.routes.ts`
- Modify: `front/src/app/components/sidebar/sidebar.ts`

- [ ] **Step 1: Create AbastecimentoPageComponent**
Integrate the vehicle selector, the table, and the "New Refill" button.

- [ ] **Step 2: Update Routes**
Register the `/abastecimentos` route.

- [ ] **Step 3: Update Sidebar**
Add the link to the Abastecimentos page.

- [ ] **Step 4: Commit**

```bash
git add front/src/app/pages/abastecimento/ front/src/app/app.routes.ts front/src/app/components/sidebar/sidebar.ts
git commit -m "feat: add abastecimento page and register routes"
```
