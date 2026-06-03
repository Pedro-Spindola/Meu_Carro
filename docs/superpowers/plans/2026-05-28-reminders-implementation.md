# Reminders (Lembrete) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a global reminder system that shows alerts for all user vehicles on the Home page, allows viewing details in a modal, and concluding them.

**Architecture:** 
- **Backend:** Extend repository with user-level queries, update DTOs with vehicle context, and add controller endpoints.
- **Frontend:** Implement dedicated models/services, refactor Home to fetch global data, and enhance the Alerts table with a detail modal and "conclude" action.

**Tech Stack:** Java (Spring Boot), Angular (TypeScript), RxJS, SCSS.

---

### Task 1: Backend Repository and DTO Updates

**Files:**
- Modify: `backend/src/main/java/com/drivenote/repository/LembreteRepository.java`
- Modify: `backend/src/main/java/com/drivenote/dto/LembreteResponse.java`

- [ ] **Step 1: Add user-level query to Repository**
Modify `LembreteRepository.java` to add a method that finds all reminders for a user's vehicles.

```java
// backend/src/main/java/com/drivenote/repository/LembreteRepository.java
List<Lembrete> findAllByVeiculoUsuarioIdOrderByDataAlertaAsc(Long usuarioId);
```

- [ ] **Step 2: Update LembreteResponse DTO**
Add vehicle context fields.

```java
// backend/src/main/java/com/drivenote/dto/LembreteResponse.java
public record LembreteResponse(
        Long id,
        Long veiculoId,
        String nomeVeiculo, // Added
        String placaVeiculo, // Added
        String titulo,
        String descricao,
        TipoLembrete tipo,
        LocalDate dataAlerta,
        StatusLembrete status,
        LocalDateTime createdAt
) {}
```

- [ ] **Step 3: Commit**
```bash
git add backend/src/main/java/com/drivenote/repository/LembreteRepository.java backend/src/main/java/com/drivenote/dto/LembreteResponse.java
git commit -m "backend: add user-level query and vehicle info to Lembrete DTO"
```

### Task 2: Backend Service and Controller

**Files:**
- Modify: `backend/src/main/java/com/drivenote/mapper/LembreteMapper.java`
- Modify: `backend/src/main/java/com/drivenote/service/LembreteService.java`
- Modify: `backend/src/main/java/com/drivenote/controller/LembreteController.java`

- [ ] **Step 1: Update Mapper**
Populate vehicle fields in `LembreteMapper.java`.

```java
// backend/src/main/java/com/drivenote/mapper/LembreteMapper.java
public static LembreteResponse toResponse(Lembrete l) {
    return new LembreteResponse(
            l.getId(),
            l.getVeiculo().getId(),
            l.getVeiculo().getModelo(), // Using modelo as name
            l.getVeiculo().getPlaca(),
            l.getTitulo(),
            l.getDescricao(),
            l.getTipo(),
            l.getDataAlerta(),
            l.getStatus(),
            l.getCreatedAt()
    );
}
```

- [ ] **Step 2: Implement User-level list in Service**
Update `LembreteService.java`.

```java
// backend/src/main/java/com/drivenote/service/LembreteService.java
public List<LembreteResponse> listarPorUsuario(Long usuarioId) {
    return repository.findAllByVeiculoUsuarioIdOrderByDataAlertaAsc(usuarioId)
            .stream().map(LembreteMapper::toResponse)
            .toList();
}
```

- [ ] **Step 3: Add Controller Endpoint**
Update `LembreteController.java`.

```java
// backend/src/main/java/com/drivenote/controller/LembreteController.java
@Operation(summary = "Listar todos os lembretes do usuário")
@GetMapping("/usuario/{usuarioId}")
public List<LembreteResponse> listarPorUsuario(@PathVariable Long usuarioId) {
    return service.listarPorUsuario(usuarioId);
}
```

- [ ] **Step 4: Commit**
```bash
git add backend/src/main/java/com/drivenote/mapper/LembreteMapper.java backend/src/main/java/com/drivenote/service/LembreteService.java backend/src/main/java/com/drivenote/controller/LembreteController.java
git commit -m "backend: implement user-level reminders endpoint"
```

### Task 3: Frontend Models and Service

**Files:**
- Create: `front/src/app/models/lembrete.model.ts`
- Create: `front/src/app/services/lembrete.service.ts`

- [ ] **Step 1: Create Lembrete Model**
```typescript
// front/src/app/models/lembrete.model.ts
export type TipoLembrete = 'MANUTENCAO' | 'DOCUMENTO' | 'SEGURO' | 'IPVA' | 'LICENCIAMENTO' | 'VIAGEM' | 'OUTRO';
export type StatusLembrete = 'PENDENTE' | 'CONCLUIDO' | 'ATRASADO';

export interface LembreteResponse {
  id: number;
  veiculoId: number;
  nomeVeiculo: string;
  placaVeiculo: string;
  titulo: string;
  descricao: string;
  tipo: TipoLembrete;
  dataAlerta: string;
  status: StatusLembrete;
  createdAt: string;
}
```

- [ ] **Step 2: Create Lembrete Service**
```typescript
// front/src/app/services/lembrete.service.ts
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LembreteResponse } from '../models/lembrete.model';

@Injectable({
  providedIn: 'root'
})
export class LembreteService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/lembretes`;

  listarPorUsuario(usuarioId: number): Observable<LembreteResponse[]> {
    return this.http.get<LembreteResponse[]>(`${this.API_URL}/usuario/${usuarioId}`);
  }

  concluir(id: number): Observable<LembreteResponse> {
    return this.http.put<LembreteResponse>(`${this.API_URL}/${id}/concluir`, {});
  }
}
```

- [ ] **Step 3: Commit**
```bash
git add front/src/app/models/lembrete.model.ts front/src/app/services/lembrete.service.ts
git commit -m "front: add lembrete model and service"
```

### Task 4: UI Cleanup and Detail Modal

**Files:**
- Modify: `front/src/app/components/sidebar/sidebar.ts`
- Create: `front/src/app/components/lembrete-detail-modal/` (ts, html, scss)

- [ ] **Step 1: Remove Reminders from Sidebar**
Remove the entry in `menuItems`.

- [ ] **Step 2: Create LembreteDetailModalComponent**
Implement a simple modal displaying all `LembreteResponse` fields.

- [ ] **Step 3: Commit**
```bash
git add front/src/app/components/sidebar/sidebar.ts front/src/app/components/lembrete-detail-modal/
git commit -m "front: remove sidebar link and create detail modal"
```

### Task 5: Alerts Table Enhancement

**Files:**
- Modify: `front/src/app/components/alerts-table/alerts-table.ts`
- Modify: `front/src/app/components/alerts-table/alerts-table.html`
- Modify: `front/src/app/components/alerts-table/alerts-table.scss`

- [ ] **Step 1: Update Input type and Add Actions**
Change input to `LembreteResponse[]` and add `onConcluir` and `onViewDetails` emitters.

- [ ] **Step 2: Update Template**
Add "Carro" column, "Ações" column with Check button, and click handler on row for details.

- [ ] **Step 3: Commit**
```bash
git add front/src/app/components/alerts-table/
git commit -m "front: enhance alerts table with actions and vehicle info"
```

### Task 6: Home Page Integration

**Files:**
- Modify: `front/src/app/pages/home/home.ts`
- Modify: `front/src/app/pages/home/home.html`

- [ ] **Step 1: Fetch Global Reminders**
In `HomeComponent`, inject `LembreteService` and fetch reminders for the user regardless of selected vehicle.

- [ ] **Step 2: Handle Conclude and Detail Actions**
Implement methods to call `lembreteService.concluir()` and open the detail modal.

- [ ] **Step 3: Commit**
```bash
git add front/src/app/pages/home/
git commit -m "front: integrate global reminders on home page"
```
