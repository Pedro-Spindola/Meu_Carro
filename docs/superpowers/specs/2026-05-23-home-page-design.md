# Design Spec - Home Page and Routing Refactor

The goal of this task is to create a new Home page and refactor the existing routing and login logic to use `/home` instead of `/dashboard`.

## Proposed Changes

### 1. Home Component
Create a new standalone component at `front/src/app/pages/home/`.
- **home.ts**: Standalone Angular component.
- **home.html**: HTML template containing `<h2>Home</h2>`.
- **home.scss**: Component styles.

### 2. Application Routes
Update `front/src/app/app.routes.ts` to include the `/home` route.
- Add `{ path: 'home', loadComponent: () => import('./pages/home/home').then(m => m.HomeComponent) }`.

### 3. Login Redirection
Update the `LoginComponent` at `front/src/app/pages/login/login.ts` to redirect successful logins to `/home`.
- Change `this.router.navigate(['/dashboard'])` to `this.router.navigate(['/home'])`.

## Success Criteria
- Navigating to `/home` renders the Home component with the "Home" heading.
- A successful login redirects the user to the `/home` page.
- No references to `/dashboard` remain in the routing or login logic.
