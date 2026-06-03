# Login Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a responsive, theme-aware login page with two columns (1fr login, 2fr image) on desktop and a single column on mobile, integrated with the Spring Boot backend.

**Architecture:** Angular standalone component using Reactive Forms for validation, a dedicated AuthService for API communication, and CSS Grid for layout.

**Tech Stack:** Angular (Standalone), SCSS, RxJS, Reactive Forms.

---

### Task 1: Auth Model

**Files:**
- Create: `front/src/app/models/auth.model.ts`

- [ ] **Step 1: Create AuthRequest and AuthResponse interfaces**

```typescript
export interface AuthRequest {
  email: string;
  senha: string;
}

export interface AuthResponse {
  token: string;
}
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/models/auth.model.ts
git commit -m "feat: add auth models"
```

---

### Task 2: Auth Service

**Files:**
- Create: `front/src/app/services/auth.service.ts`

- [ ] **Step 1: Implement AuthService with login method**

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthRequest, AuthResponse } from '../models/auth.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = '/api/auth';

  constructor(private http: HttpClient) {}

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, request);
  }
}
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/services/auth.service.ts
git commit -m "feat: add auth service"
```

---

### Task 3: Login Component Scaffolding

**Files:**
- Create: `front/src/app/pages/login/login.ts`
- Create: `front/src/app/pages/login/login.html`
- Create: `front/src/app/pages/login/login.scss`

- [ ] **Step 1: Define Login Component TS**

```typescript
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    public themeService: ThemeService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid || this.isLoading) return;

    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        // TODO: Save token (usually in a dedicated StorageService)
        localStorage.setItem('token', res.token);
        this.router.navigate(['/dashboard']);
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Email ou senha inválidos.';
        this.isLoading = false;
      }
    });
  }
}
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/pages/login/login.ts
git commit -m "feat: scaffold login component logic"
```

---

### Task 4: Login Component Layout (2 Columns)

**Files:**
- Modify: `front/src/app/pages/login/login.html`
- Modify: `front/src/app/pages/login/login.scss`

- [ ] **Step 1: Implement HTML Structure**

```html
<div class="login-container">
  <div class="login-form-side">
    <div class="login-content">
      <img [src]="themeService.logoUrl" alt="DriveNote Logo" class="logo">
      
      <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" formControlName="email" placeholder="Seu email">
        </div>

        <div class="form-group">
          <label for="senha">Senha</label>
          <input type="password" id="senha" formControlName="senha" placeholder="Sua senha">
        </div>

        <div class="login-actions">
          <a href="#">Esqueci minha senha</a>
          <a href="#">Novo usuário</a>
        </div>

        <p class="error-message" *ngIf="errorMessage">{{ errorMessage }}</p>

        <button type="submit" [disabled]="loginForm.invalid || isLoading">
          <span *ngIf="!isLoading">Entrar</span>
          <span *ngIf="isLoading">Carregando...</span>
        </button>
      </form>
    </div>
  </div>
  
  <div class="login-image-side">
    <!-- Image background via SCSS -->
  </div>
</div>
```

- [ ] **Step 2: Implement SCSS Styling**

```scss
@use 'src/styles/colors' as *;

.login-container {
  display: grid;
  grid-template-columns: 1fr 2fr;
  min-height: 100vh;
  background-color: map-get(map-get($colors, dark), background); // Default background

  @media (max-width: 768px) {
    grid-template-columns: 1fr;
  }
}

.login-form-side {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  background-color: var(--surface-color, #1E1E1E);

  .login-content {
    width: 100%;
    max-width: 400px;
    display: flex;
    flex-direction: column;
    gap: 2rem;
  }
}

.logo {
  max-width: 200px;
  margin: 0 auto;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  label {
    font-size: 0.9rem;
    color: var(--text-secondary-color, #B0B0B0);
  }

  input {
    padding: 0.8rem;
    border-radius: 4px;
    border: 1px solid var(--border-color, #333);
    background-color: transparent;
    color: var(--text-color, #FFF);
    outline: none;

    &:focus {
      border-color: var(--primary-color, #FF9800);
    }
  }
}

.login-actions {
  display: flex;
  justify-content: space-between;
  font-size: 0.85rem;

  a {
    color: var(--primary-color, #FF9800);
    text-decoration: none;
    &:hover { text-decoration: underline; }
  }
}

.error-message {
  color: #ff5252;
  font-size: 0.85rem;
  margin: 0;
  min-height: 1.2rem;
}

button {
  padding: 1rem;
  background-color: var(--primary-color, #FF9800);
  color: white;
  border: none;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  transition: opacity 0.2s;

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}

.login-image-side {
  background: url('/api/placeholder/1200/800') center/cover no-repeat;
  @media (max-width: 768px) {
    display: none;
  }
}
```

- [ ] **Step 3: Commit**

```bash
git add front/src/app/pages/login/login.html front/src/app/pages/login/login.scss
git commit -m "feat: add login page layout and styles"
```

---

### Task 5: Routing Configuration

**Files:**
- Modify: `front/src/app/app.routes.ts`

- [ ] **Step 1: Add Login Route**

```typescript
import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.LoginComponent) },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
```

- [ ] **Step 2: Commit**

```bash
git add front/src/app/app.routes.ts
git commit -m "feat: configure login routing"
```
