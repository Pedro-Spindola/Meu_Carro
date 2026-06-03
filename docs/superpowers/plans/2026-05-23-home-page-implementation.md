# Home Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create a new Home page and refactor routing/login to use `/home` instead of `/dashboard`.

**Architecture:** Create a new standalone `HomeComponent`, register it in `app.routes.ts`, and update the `LoginComponent` redirection logic.

**Tech Stack:** Angular 21, Signals, Reactive Forms.

---

### Task 1: Create Home Component

**Files:**
- Create: `front/src/app/pages/home/home.ts`
- Create: `front/src/app/pages/home/home.html`
- Create: `front/src/app/pages/home/home.scss`

- [ ] **Step 1: Create the HTML template**
```html
<div class="home-container">
  <h2>Home</h2>
</div>
```

- [ ] **Step 2: Create the component class**
```typescript
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss']
})
export class HomeComponent {}
```

- [ ] **Step 3: Create basic styles**
```scss
.home-container {
  padding: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}
```

- [ ] **Step 4: Commit**
```bash
git add front/src/app/pages/home/
git commit -m "feat: create home component"
```

### Task 2: Register Home Route

**Files:**
- Modify: `front/src/app/app.routes.ts`

- [ ] **Step 1: Update routes configuration**
```typescript
import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./pages/login/login').then(m => m.LoginComponent) },
  { path: 'home', loadComponent: () => import('./pages/home/home').then(m => m.HomeComponent) },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
```

- [ ] **Step 2: Commit**
```bash
git add front/src/app/app.routes.ts
git commit -m "feat: register home route"
```

### Task 3: Refactor Login Redirection

**Files:**
- Modify: `front/src/app/pages/login/login.ts`

- [ ] **Step 1: Change redirection from /dashboard to /home**
```typescript
// Inside onSubmit() success handler
      next: (res) => {
        localStorage.setItem('token', res.token);
        this.router.navigate(['/home']);
        this.isLoading.set(false);
      },
```

- [ ] **Step 2: Commit**
```bash
git add front/src/app/pages/login/login.ts
git commit -m "refactor: redirect to /home after login"
```

### Task 4: Verification

- [ ] **Step 1: Verify login redirection manually**
1. Open the login page.
2. Enter valid credentials.
3. Verify the browser redirects to `http://localhost:4200/home`.
4. Verify the "Home" heading is visible.
