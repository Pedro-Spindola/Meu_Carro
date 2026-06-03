# Refactor UI Sub-Components Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor `VehicleCard`, `StatCard`, and `AlertsTable` components to use a multi-file structure (.ts, .html, .scss) and utilize CSS variables for status colors in `AlertsTable`.

**Architecture:** Split existing single-file components into standard Angular multi-file components. Update `AlertsTableComponent` to use CSS variables defined in `_colors.scss` instead of hardcoded hex values in TypeScript.

**Tech Stack:** Angular, SCSS, CSS Variables.

---

### Task 1: Refactor VehicleCardComponent

**Files:**
- Create: `front/src/app/components/vehicle-card/vehicle-card.html`
- Create: `front/src/app/components/vehicle-card/vehicle-card.scss`
- Modify: `front/src/app/components/vehicle-card/vehicle-card.ts`

- [ ] **Step 1: Create vehicle-card.html**

```html
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
```

- [ ] **Step 2: Create vehicle-card.scss**

```scss
.vehicle-card {
  padding: 1.5rem;
  border-radius: 8px;
  background-color: var(--surface-color);
  border: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.info {
  h3 {
    margin: 0;
    font-size: 1.25rem;
  }
  p {
    margin: 0.25rem 0 0;
    color: var(--text-secondary);
  }
}

.details {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}
```

- [ ] **Step 3: Modify vehicle-card.ts to use separate files**

```typescript
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-vehicle-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vehicle-card.html',
  styleUrl: './vehicle-card.scss'
})
export class VehicleCardComponent {
  @Input() name!: string;
  @Input() model!: string;
  @Input() year!: number;
  @Input() plate!: string;
  @Input() km!: number;
}
```

- [ ] **Step 4: Commit**

```bash
git add front/src/app/components/vehicle-card/
git commit -m "refactor: convert VehicleCard to multi-file component"
```

### Task 2: Refactor StatCardComponent

**Files:**
- Create: `front/src/app/components/stat-card/stat-card.html`
- Create: `front/src/app/components/stat-card/stat-card.scss`
- Modify: `front/src/app/components/stat-card/stat-card.ts`

- [ ] **Step 1: Create stat-card.html**

```html
<div class="stat-card">
  <span class="label">{{ label }}</span>
  <span class="value">{{ value }}</span>
</div>
```

- [ ] **Step 2: Create stat-card.scss**

```scss
.stat-card {
  padding: 1.25rem;
  border-radius: 8px;
  background-color: var(--surface-color);
  border: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.label {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.value {
  font-size: 1.1rem;
  font-weight: bold;
}
```

- [ ] **Step 3: Modify stat-card.ts to use separate files**

```typescript
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stat-card.html',
  styleUrl: './stat-card.scss'
})
export class StatCardComponent {
  @Input() label!: string;
  @Input() value!: string | number;
}
```

- [ ] **Step 4: Commit**

```bash
git add front/src/app/components/stat-card/
git commit -m "refactor: convert StatCard to multi-file component"
```

### Task 3: Refactor AlertsTableComponent and Use CSS Variables for Status

**Files:**
- Create: `front/src/app/components/alerts-table/alerts-table.html`
- Create: `front/src/app/components/alerts-table/alerts-table.scss`
- Modify: `front/src/app/components/alerts-table/alerts-table.ts`

- [ ] **Step 1: Create alerts-table.html**

```html
<div class="alerts-container">
  <h3>Alertas e Notificações</h3>
  <table class="alerts-table">
    <thead>
      <tr>
        <th>Título</th>
        <th>Data</th>
        <th>Status</th>
      </tr>
    </thead>
    <tbody>
      <tr *ngFor="let alert of alerts">
        <td>{{ alert.titulo }}</td>
        <td>{{ alert.dataAlerta | date:'dd/MM/yyyy' }}</td>
        <td>
          <span class="status-badge" [ngClass]="alert.status.toLowerCase()">
            {{ alert.status }}
          </span>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

- [ ] **Step 2: Create alerts-table.scss**

```scss
.alerts-container {
  margin-top: 2rem;
}

.alerts-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;

  th, td {
    padding: 1rem;
    text-align: left;
    border-bottom: 1px solid var(--border-color);
  }
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  color: white;
  font-weight: bold;

  &.pendente {
    background-color: var(--status-pending-color);
  }

  &.atrasado {
    background-color: var(--status-delayed-color);
  }

  &.concluido {
    background-color: var(--status-completed-color);
  }
}
```

- [ ] **Step 3: Modify alerts-table.ts to use separate files and remove getStatusColor**

```typescript
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LembreteResumo } from '../../models/dashboard.model';

@Component({
  selector: 'app-alerts-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alerts-table.html',
  styleUrl: './alerts-table.scss'
})
export class AlertsTableComponent {
  @Input() alerts: LembreteResumo[] = [];
}
```

- [ ] **Step 4: Commit**

```bash
git add front/src/app/components/alerts-table/
git commit -m "refactor: convert AlertsTable to multi-file component and use CSS variables for status"
```

### Task 4: Verification

- [ ] **Step 1: Run build to ensure no errors**

Run: `npm run build` (in front directory)
Expected: Successful build.

- [ ] **Step 2: Final Commit**

```bash
git commit --allow-empty -m "chore: finish refactoring UI sub-components"
```
