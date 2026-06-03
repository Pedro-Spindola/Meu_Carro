# Vehicle Models and Service Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create vehicle models and service for the "Veículos" page.

**Architecture:** Angular models (interfaces/enums) for type safety and an Angular service utilizing HttpClient for API communication.

**Tech Stack:** Angular, TypeScript, RxJS.

---

### Task 1: Define Vehicle Models

**Files:**
- Create: `front/src/app/models/veiculo.model.ts`

- [ ] **Step 1: Create `front/src/app/models/veiculo.model.ts`**

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

- [ ] **Step 2: Commit**

```bash
git add front/src/app/models/veiculo.model.ts
git commit -m "feat: add vehicle models"
```

### Task 2: Implement VeiculoService

**Files:**
- Create: `front/src/app/services/veiculo.service.ts`

- [ ] **Step 1: Create `front/src/app/services/veiculo.service.ts`**

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

- [ ] **Step 2: Syntax Check**

Run: `npx tsc --noEmit front/src/app/services/veiculo.service.ts --esModuleInterop --skipLibCheck --target esnext --moduleResolution node`

- [ ] **Step 3: Commit**

```bash
git add front/src/app/services/veiculo.service.ts
git commit -m "feat: add vehicle service"
```
