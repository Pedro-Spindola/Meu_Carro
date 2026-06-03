# Profile Page Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the Profile page with user data editing, password change, and preferences (theme and default vehicle).

**Architecture:** 
- **Backend:** CRUD for user profile and password change logic.
- **Frontend:** Dedicated page with sections for different settings, utilizing Signals for state and localStorage for persistence of client-side preferences.

**Tech Stack:** Java (Spring Boot), Angular (TypeScript), SCSS.

---

### Task 1: Backend User Management Updates

**Files:**
- Create: `backend/src/main/java/com/drivenote/dto/SenhaUpdateRequest.java`
- Modify: `backend/src/main/java/com/drivenote/service/UsuarioService.java`
- Modify: `backend/src/main/java/com/drivenote/controller/UsuarioController.java`

- [ ] **Step 1: Create SenhaUpdateRequest DTO**
```java
package com.drivenote.dto;
import jakarta.validation.constraints.*;
public record SenhaUpdateRequest(
    @NotBlank String senhaAtual,
    @NotBlank @Size(min = 6) String novaSenha
) {}
```

- [ ] **Step 2: Implement password change in UsuarioService**
```java
public void alterarSenha(Long id, SenhaUpdateRequest request) {
    Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
        throw new BusinessException("Senha atual incorreta");
    }
    usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
    repository.save(usuario);
}
```

- [ ] **Step 3: Update UsuarioController**
Add `@GetMapping("/{id}")`, `@PutMapping("/{id}")`, and `@PutMapping("/{id}/senha")`.

- [ ] **Step 4: Commit**
```bash
git add backend/
git commit -m "backend: add user update and password change endpoints"
```

### Task 2: Frontend Models and Service

**Files:**
- Create: `front/src/app/models/usuario.model.ts`
- Create: `front/src/app/services/usuario.service.ts`

- [ ] **Step 1: Create Usuario models**
Define `UsuarioResponse`, `UsuarioUpdateRequest`, and `SenhaUpdateRequest`.

- [ ] **Step 2: Create UsuarioService**
Implement `buscarPorId`, `atualizar`, and `alterarSenha`.

- [ ] **Step 3: Commit**
```bash
git add front/src/app/models/usuario.model.ts front/src/app/services/usuario.service.ts
git commit -m "front: add user model and service"
```

### Task 3: Profile Page Implementation

**Files:**
- Create: `front/src/app/pages/perfil/` (ts, html, scss)
- Modify: `front/src/app/app.routes.ts`
- Modify: `front/src/app/components/sidebar/sidebar.ts`

- [ ] **Step 1: Create PerfilPageComponent**
Implement three forms/sections: Personal Data, Security, and Preferences.

- [ ] **Step 2: Implement Theme Toggle**
Inject `ThemeService` and bind the toggle to `themeService.setTheme()`. Ensure `ThemeService` persists to localStorage.

- [ ] **Step 3: Implement Default Vehicle**
Fetch vehicle list. Save selected vehicle ID to `localStorage.setItem('preferredVehicleId', id)`.

- [ ] **Step 4: Update Sidebar and Routes**
Add "Perfil" to sidebar (bottom or top) and register the `/perfil` route.

- [ ] **Step 5: Commit**
```bash
git add front/src/app/pages/perfil/ front/src/app/app.routes.ts front/src/app/components/sidebar/sidebar.ts
git commit -m "front: implement profile page and navigation"
```

### Task 4: Home Page Default Vehicle Logic

**Files:**
- Modify: `front/src/app/pages/home/home.ts`

- [ ] **Step 1: Update Home redirection**
In `ngOnInit`, check `localStorage.getItem('preferredVehicleId')`. If present, use it to load the dashboard instead of the first vehicle in the list.

- [ ] **Step 2: Commit**
```bash
git add front/src/app/pages/home/home.ts
git commit -m "front: support preferred vehicle on home page"
```
