# Login Page Design Specification

**Feature:** User Authentication (Login)
**Date:** 2026-05-23
**Status:** Draft

## 1. Overview
The login page is the entry point for the MeuCarro application. It provides a split-screen interface on desktop and a focused single-column view on mobile.

## 2. Layout & UI
### 2.1 Desktop Layout
- **Container:** CSS Grid with two columns.
- **Column 1 (Login Form):** `1fr`. Contains logo, inputs, and actions.
- **Column 2 (Hero Image):** `2fr`. Reserved for a background image.
- **Backgrounds:** Uses theme-aware colors from `_colors.scss` (`surface` for form, `background` for hero area).

### 2.2 Mobile Layout
- **Column 1:** Occupies `100%` width.
- **Column 2:** Hidden (`display: none`).

### 2.3 Visual Elements
- **Logo:** Automatically switches between `public/logo.png` (light) and `public/logo_w.png` (dark) via `ThemeService`.
- **Primary Color:** Orange (`#FF9800`) as per `_colors.scss`.

## 3. Components & Architecture
### 3.1 Data Models (`src/app/models/auth.model.ts`)
Matches Backend DTOs:
- `AuthRequest`: `{ email: string, senha: string }`
- `AuthResponse`: `{ token: string }`

### 3.2 Authentication Service (`src/app/services/auth.service.ts`)
- `login(request: AuthRequest): Observable<AuthResponse>`
- Handles API call to `/api/auth/login`.

### 3.3 Login Component (`src/app/pages/login/`)
- **Type:** Angular Standalone Component.
- **Form:** `ReactiveFormsModule`.
  - Email: `required`, `email`.
  - Senha: `required`.
- **State Management:**
  - `isLoading`: Boolean to disable the login button and prevent double submissions.
  - `errorMessage`: String to display backend/validation errors in red.

## 4. User Interactions
- **Submit:** Triggers `login()` in the service.
- **Validation:** Show errors if fields are touched and invalid.
- **Forgot Password:** Link to password recovery (TBD).
- **Register:** Link to account creation (TBD).

## 5. Success Criteria
- Responsive layout (Mobile/Desktop).
- Successful authentication redirects user (Path TBD, usually Dashboard).
- Logo updates correctly when theme toggles.
- No multiple concurrent login requests possible.
