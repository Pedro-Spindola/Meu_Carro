# Maintenance Feature Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a dual-section Maintenance page for managing preventive plans and recording maintenance history.

**Architecture:** Following the project's Angular patterns: standalone components, reactive forms in modais, and dedicated services for API interaction.

**Tech Stack:** Angular (TypeScript), RxJS, Signals, SCSS.

---

### Task 1: Models and Services

**Files:**
- Create: `front/src/app/models/manutencao.model.ts`
- Create: `front/src/app/models/plano-manutencao.model.ts`
- Create: `front/src/app/services/manutencao.service.ts`
- Create: `front/src/app/services/plano-manutencao.service.ts`

- [ ] **Step 1: Create Maintenance Models**
Define `TipoManutencao` enum and interfaces for `ManutencaoResponse/CreateRequest` and `PlanoManutencaoResponse/CreateRequest`.

- [ ] **Step 2: Create Services**
Implement `ManutencaoService` and `PlanoManutencaoService` with methods to list by vehicle and create records/plans.

- [ ] **Step 3: Commit**

```bash
git add front/src/app/models/ front/src/app/services/
git commit -m "feat: add maintenance models and services"
```

### Task 2: Prevention Cards and History Table

**Files:**
- Create: `front/src/app/components/plano-card/` (ts, html, scss)
- Create: `front/src/app/components/manutencao-table/` (ts, html, scss)

- [ ] **Step 1: Create PlanoCardComponent**
Implement a card that displays the maintenance type, status (using the logic defined in the spec), and a "Gerenciar" button.

- [ ] **Step 2: Create ManutencaoTableComponent**
Implement a table for the maintenance history, similar to the `AbastecimentoTable`.

- [ ] **Step 3: Commit**

```bash
git add front/src/app/components/plano-card/ front/src/app/components/manutencao-table/
git commit -m "feat: add maintenance UI components"
```

### Task 3: Maintenance and Plan Forms (Modals)

**Files:**
- Create: `front/src/app/components/plano-form/` (ts, html, scss)
- Create: `front/src/app/components/manutencao-form/` (ts, html, scss)

- [ ] **Step 1: Create PlanoFormComponent**
Reactive form for configuring `intervaloKm`, `intervaloDias`, and `ativo` status.

- [ ] **Step 2: Create ManutencaoFormComponent**
Reactive form for registering maintenance with `data`, `quilometragem`, `descricao`, and `valor`.

- [ ] **Step 3: Commit**

```bash
git add front/src/app/components/plano-form/ front/src/app/components/manutencao-form/
git commit -m "feat: add maintenance modals and forms"
```

### Task 4: Main Page and Routing

**Files:**
- Create: `front/src/app/pages/manutencao/` (ts, html, scss)
- Modify: `front/src/app/app.routes.ts`
- Modify: `front/src/app/components/sidebar/sidebar.ts`

- [ ] **Step 1: Create ManutencaoPageComponent**
Coordinate the vehicle selector, the grid of `PlanoCardComponent`, and the `ManutencaoTableComponent`. Handle modal triggers.

- [ ] **Step 2: Update Routes and Sidebar**
Register the `/manutencoes` route and add it to the sidebar navigation.

- [ ] **Step 3: Commit**

```bash
git add front/src/app/pages/manutencao/ front/src/app/app.routes.ts front/src/app/components/sidebar/sidebar.ts
git commit -m "feat: integrate maintenance page and routes"
```
