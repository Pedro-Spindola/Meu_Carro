# Frontend Models and Service Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create `Usuario` models and service in Angular to interact with backend endpoints for fetching, updating user info, and changing password.

**Architecture:** Define TypeScript interfaces for backend DTOs and an Angular service using `HttpClient` to communicate with the REST API.

**Tech Stack:** Angular, TypeScript, RxJS

---

### Task 1: Create Usuario Models

**Files:**
- Create: `front/src/app/models/usuario.model.ts`

- [ ] **Step 1: Define UsuarioResponse, UsuarioUpdateRequest, and SenhaUpdateRequest interfaces**

```typescript
export interface UsuarioResponse {
  id: number;
  nome: string;
  email: string;
  telefone: string;
  createdAt: string;
  updatedAt: string;
}

export interface UsuarioUpdateRequest {
  nome: string;
  telefone: string;
}

export interface SenhaUpdateRequest {
  senhaAtual: string;
  novaSenha: string;
}
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/models/usuario.model.ts
git commit -m "feat(front): add usuario models"
```

### Task 2: Create UsuarioService

**Files:**
- Create: `front/src/app/services/usuario.service.ts`

- [ ] **Step 1: Implement UsuarioService using inject(HttpClient)**

```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioResponse, UsuarioUpdateRequest, SenhaUpdateRequest } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/usuarios`;

  buscarPorId(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.API_URL}/${id}`);
  }

  atualizar(id: number, request: UsuarioUpdateRequest): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.API_URL}/${id}`, request);
  }

  alterarSenha(id: number, request: SenhaUpdateRequest): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/senha`, request);
  }
}
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/services/usuario.service.ts
git commit -m "feat(front): add usuario service"
```

### Task 3: Verification

- [ ] **Step 1: Run Angular lint/build check**

Run: `cd front; npm run build` (or `ng build` if available)
Expected: No compilation errors.

- [ ] **Step 2: Commit**

```bash
git commit --allow-empty -m "vouch: frontend models and service verified"
```
