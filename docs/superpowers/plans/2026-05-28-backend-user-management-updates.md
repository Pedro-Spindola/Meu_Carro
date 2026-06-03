# Backend User Management Updates Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update backend user management with DTO for password update, implement password change logic in service, and expose new endpoints in controller.

**Architecture:** Use Spring Boot REST controllers, services, and DTOs (records). Follow existing patterns for mapping and exception handling.

**Tech Stack:** Java 17, Spring Boot 3, Spring Security, Jakarta Validation, Lombok.

---

### Task 1: Create SenhaUpdateRequest DTO

**Files:**
- Create: `backend/src/main/java/com/drivenote/dto/SenhaUpdateRequest.java`

- [ ] **Step 1: Create the DTO record**

```java
package com.drivenote.dto;

import jakarta.validation.constraints.*;

public record SenhaUpdateRequest(
    @NotBlank(message = "Senha atual é obrigatória")
    String senhaAtual,

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "Nova senha deve ter no mínimo 6 caracteres")
    String novaSenha
) {}
```

- [ ] **Step 2: Commit**

### Task 2: Implement password change in UsuarioService

**Files:**
- Modify: `backend/src/main/java/com/drivenote/service/UsuarioService.java`

- [ ] **Step 1: Implement `alterarSenha` method**

```java
    /**
     * Altera a senha do usuário.
     */
    @Transactional
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

- [ ] **Step 2: Commit**

### Task 3: Update UsuarioController

**Files:**
- Modify: `backend/src/main/java/com/drivenote/controller/UsuarioController.java`

- [ ] **Step 1: Add new endpoints**

```java
    @Operation(summary = "Buscar usuário por ID")
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @Operation(summary = "Atualizar usuário")
    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @Operation(summary = "Alterar senha")
    @PutMapping("/{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaUpdateRequest request) {
        service.alterarSenha(id, request);
    }
```

- [ ] **Step 2: Commit**

### Task 4: Verification

- [ ] **Step 1: Create/Update integration tests in `UsuarioControllerIT`**
- [ ] **Step 2: Run tests**
- [ ] **Step 3: Commit**
