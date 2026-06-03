# Refactor AlertsTableComponent Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor `AlertsTableComponent` into a multi-file structure (`.ts`, `.html`, `.scss`) and use theme-based CSS variables for status colors.

**Architecture:** Split the inline template and styles into separate files. Update the TypeScript component to reference these files and remove manual color logic in favor of CSS classes.

**Tech Stack:** Angular 19, TypeScript, SCSS.

---

### Task 1: Create SCSS File

**Files:**
- Create: `front/src/app/components/alerts-table/alerts-table.scss`

- [ ] **Step 1: Create SCSS with theme variables**

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
    color: var(--text-color);
  }

  th {
    color: var(--text-secondary-color);
    font-weight: 600;
  }
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 12px;
  font-size: 0.8rem;
  color: white;
  font-weight: bold;
  display: inline-block;

  &.status-pending {
    background-color: var(--status-pending-color);
  }

  &.status-delayed {
    background-color: var(--status-delayed-color);
  }

  &.status-completed {
    background-color: var(--status-completed-color);
  }
}
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/components/alerts-table/alerts-table.scss
git commit -m "style: add SCSS for AlertsTableComponent"
```

---

### Task 2: Create HTML Template File

**Files:**
- Create: `front/src/app/components/alerts-table/alerts-table.html`

- [ ] **Step 1: Create HTML template with status classes**

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
          <span class="status-badge" [ngClass]="getStatusClass(alert.status)">
            {{ alert.status }}
          </span>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/components/alerts-table/alerts-table.html
git commit -m "feat: add HTML template for AlertsTableComponent"
```

---

### Task 3: Update TypeScript Component

**Files:**
- Modify: `front/src/app/components/alerts-table/alerts-table.ts`

- [ ] **Step 1: Update component to use external files and getStatusClass**

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

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDENTE': return 'status-pending';
      case 'ATRASADO': return 'status-delayed';
      case 'CONCLUIDO': return 'status-completed';
      default: return '';
    }
  }
}
```

- [ ] **Step 2: Verify compilation**

Run: `npx tsc -p front/tsconfig.app.json --noEmit`
Expected: SUCCESS

- [ ] **Step 3: Commit**

```bash
git add front/src/app/components/alerts-table/alerts-table.ts
git commit -m "refactor: move AlertsTableComponent to multi-file structure"
```

---

### Task 4: Final Verification

- [ ] **Step 1: Check other refactored components for consistency**
- [ ] **Step 2: Commit any final tweaks**
