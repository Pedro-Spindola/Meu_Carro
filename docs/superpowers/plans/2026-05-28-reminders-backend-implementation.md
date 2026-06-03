# Backend Service and Controller Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement the backend service and controller methods to list reminders by user ID.

**Architecture:** Extend existing `LembreteService` and `LembreteController` to include a method for listing reminders filtering by the user associated with the vehicle.

**Tech Stack:** Java 17, Spring Boot 3, Spring Data JPA.

---

### Task 1: Update LembreteService

**Files:**
- Modify: `backend/src/main/java/com/drivenote/service/LembreteService.java`

- [ ] **Step 1: Implement listarPorUsuario**

```java
    public List<LembreteResponse> listarPorUsuario(Long usuarioId) {
        return repository.findAllByVeiculoUsuarioIdOrderByDataAlertaAsc(usuarioId)
                .stream().map(LembreteMapper::toResponse)
                .toList();
    }
```

- [ ] **Step 2: Verify compilation**
Run: `mvn clean compile` in the backend directory.

### Task 2: Update LembreteController

**Files:**
- Modify: `backend/src/main/java/com/drivenote/controller/LembreteController.java`

- [ ] **Step 1: Add listarPorUsuario endpoint**

```java
    @Operation(summary = "Listar todos os lembretes do usuário")
    @GetMapping("/usuario/{usuarioId}")
    public List<LembreteResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }
```

- [ ] **Step 2: Verify compilation**
Run: `mvn clean compile` in the backend directory.
