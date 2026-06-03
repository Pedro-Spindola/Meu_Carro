# Alerts Table Enhancement Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Enhance the Alerts Table component with more data and actions.

**Architecture:** Update Input type to `LembreteResponse`, add Output emitters for actions, and update template/styles.

**Tech Stack:** Angular 19, SCSS, Material Icons.

---

### Task 1: Update Component Logic

**Files:**
- Modify: `front/src/app/components/alerts-table/alerts-table.ts`

- [ ] **Step 1: Update imports and Input/Output**

```typescript
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LembreteResponse } from '../../models/lembrete.model';

@Component({
  selector: 'app-alerts-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alerts-table.html',
  styleUrl: './alerts-table.scss'
})
export class AlertsTableComponent {
  @Input() alerts: LembreteResponse[] = [];
  @Output() onConcluir = new EventEmitter<number>();
  @Output() onViewDetails = new EventEmitter<LembreteResponse>();

  getStatusClass(status: string): string {
    switch (status) {
      case 'PENDENTE': return 'status-pending';
      case 'ATRASADO': return 'status-delayed';
      case 'CONCLUIDO': return 'status-completed';
      default: return '';
    }
  }

  concluir(id: number, event: Event): void {
    event.stopPropagation();
    this.onConcluir.emit(id);
  }

  viewDetails(alert: LembreteResponse): void {
    this.onViewDetails.emit(alert);
  }
}
```

### Task 2: Update Template

**Files:**
- Modify: `front/src/app/components/alerts-table/alerts-table.html`

- [ ] **Step 1: Add "Carro" and "Ações" columns**

```html
<div class="alerts-container">
  <h3>Alertas e Notificações</h3>
  <table class="alerts-table">
    <thead>
      <tr>
        <th>Carro</th>
        <th>Título</th>
        <th>Data</th>
        <th>Status</th>
        <th class="col-actions">Ações</th>
      </tr>
    </thead>
    <tbody>
      <tr *ngFor="let alert of alerts" (click)="viewDetails(alert)" class="clickable-row">
        <td class="col-vehicle">{{ alert.nomeVeiculo }}</td>
        <td class="col-title">
          <div class="title-with-icon">
            <span class="material-icons">notifications</span>
            {{ alert.titulo }}
          </div>
        </td>
        <td class="col-date">{{ alert.dataAlerta | date:'dd/MM/yyyy' }}</td>
        <td class="col-status">
          <span class="status-badge" [ngClass]="getStatusClass(alert.status)">
            {{ alert.status }}
          </span>
        </td>
        <td class="col-actions">
          <button 
            *ngIf="alert.status !== 'CONCLUIDO'"
            class="action-btn conclude-btn" 
            (click)="concluir(alert.id, $event)"
            title="Concluir"
          >
            <span class="material-icons">check_circle</span>
          </button>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

### Task 3: Update Styles

**Files:**
- Modify: `front/src/app/components/alerts-table/alerts-table.scss`

- [ ] **Step 1: Style the new columns and actions**

```scss
.alerts-container {
  h3 {
    margin: 0 0 1.5rem;
    font-size: 1.1rem;
    font-weight: 600;
    color: var(--text-color);
  }
}

.alerts-table {
  width: 100%;
  border-collapse: collapse;

  th {
    padding: 0.75rem 1rem;
    text-align: left;
    font-size: 0.8rem;
    font-weight: 600;
    color: var(--text-secondary-color);
    text-transform: uppercase;
    letter-spacing: 0.05em;
    border-bottom: 2px solid var(--border-color);
  }

  td {
    padding: 1.25rem 1rem;
    text-align: left;
    border-bottom: 1px solid var(--border-color);
    color: var(--text-color);
    font-size: 0.9rem;
    vertical-align: middle;
  }

  tr:last-child td {
    border-bottom: none;
  }

  .clickable-row {
    cursor: pointer;
    transition: background-color 0.2s;

    &:hover {
      background-color: rgba(0, 0, 0, 0.02);
    }
  }

  .col-vehicle { 
    width: 150px; 
    font-weight: 500;
  }
  .col-title { width: auto; }
  .col-date { width: 120px; color: var(--text-secondary-color); }
  .col-status { width: 120px; }
  .col-actions { 
    width: 80px; 
    text-align: center;
  }

  .title-with-icon {
    display: flex;
    align-items: center;
    gap: 1rem;

    .material-icons {
      font-size: 20px;
      color: var(--primary-color);
      opacity: 0.8;
    }
  }

  .action-btn {
    background: none;
    border: none;
    padding: 4px;
    cursor: pointer;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
    color: var(--text-secondary-color);

    &:hover {
      background-color: rgba(0, 0, 0, 0.05);
      color: var(--primary-color);
    }

    &.conclude-btn:hover {
      color: #4caf50;
      background-color: rgba(76, 175, 80, 0.1);
    }

    .material-icons {
      font-size: 20px;
    }
  }
}

.status-badge {
  padding: 0.35rem 0.75rem;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 700;
  display: inline-block;
  text-transform: uppercase;

  &.status-pending {
    background-color: rgba(156, 39, 176, 0.1);
    color: #9c27b0;
  }

  &.status-delayed {
    background-color: rgba(244, 67, 54, 0.1);
    color: #f44336;
  }

  &.status-completed {
    background-color: rgba(76, 175, 80, 0.1);
    color: #4caf50;
  }
}

@media (max-width: 768px) {
  .alerts-table {
    .col-vehicle, .col-date {
      display: none;
    }
  }
}
```
