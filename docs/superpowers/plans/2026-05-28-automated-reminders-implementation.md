# Automated Maintenance Reminders Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Automatically generate reminders for overdue maintenance plans and provide an endpoint for manual triggering.

**Architecture:** 
- **Repository:** Add check for duplicate reminders.
- **Service:** Implement overdue detection logic (KM and Days).
- **Automation:** Update Scheduler and add Controller endpoint.

**Tech Stack:** Java (Spring Boot, Spring Data JPA).

---

### Task 1: Repository Enhancement

**Files:**
- Modify: `backend/src/main/java/com/drivenote/repository/LembreteRepository.java`

- [ ] **Step 1: Add existence check method**
Add a method to check if a reminder with a specific title already exists for a vehicle in an active status.

```java
// backend/src/main/java/com/drivenote/repository/LembreteRepository.java
boolean existsByVeiculoIdAndTituloAndStatusIn(Long veiculoId, String titulo, java.util.Collection<com.drivenote.enums.StatusLembrete> statuses);
```

- [ ] **Step 2: Commit**
```bash
git add backend/src/main/java/com/drivenote/repository/LembreteRepository.java
git commit -m "backend: add duplicate check to LembreteRepository"
```

### Task 2: Automation Logic in Service

**Files:**
- Modify: `backend/src/main/java/com/drivenote/service/LembreteService.java`

- [ ] **Step 1: Inject dependencies**
Inject `PlanoManutencaoRepository` and `ManutencaoRepository`.

- [ ] **Step 2: Implement overdue detection logic**
Implement `gerarLembretesManutencaoVencida()`.

```java
// backend/src/main/java/com/drivenote/service/LembreteService.java
@org.springframework.transaction.annotation.Transactional
public void gerarLembretesManutencaoVencida() {
    List<com.drivenote.entity.PlanoManutencao> planos = planoRepository.findAll(); // simplified, ideally only active ones
    for (var plano : planos) {
        if (!plano.isAtivo()) continue;
        
        var veiculo = plano.getVeiculo();
        var ultima = manutencaoRepository.findTopByPlanoManutencaoIdOrderByDataDesc(plano.getId());
        
        boolean vencido = false;
        if (plano.getIntervaloKm() != null) {
            long baseKm = ultima.map(com.drivenote.entity.Manutencao::getQuilometragem).orElse(0L);
            if (veiculo.getQuilometragemAtual() >= baseKm + plano.getIntervaloKm()) vencido = true;
        }
        if (!vencido && plano.getIntervaloDias() != null) {
            java.time.LocalDate baseData = ultima.map(com.drivenote.entity.Manutencao::getData).orElse(veiculo.getCreatedAt().toLocalDate());
            if (java.time.LocalDate.now().isAfter(baseData.plusDays(plano.getIntervaloDias()))) vencido = true;
        }

        if (vencido) {
            String titulo = "Manutenção Vencida: " + plano.getTipo();
            boolean jaExiste = repository.existsByVeiculoIdAndTituloAndStatusIn(
                veiculo.getId(), titulo, List.of(com.drivenote.enums.StatusLembrete.PENDENTE, com.drivenote.enums.StatusLembrete.ATRASADO));
            
            if (!jaExiste) {
                repository.save(com.drivenote.entity.Lembrete.builder()
                    .veiculo(veiculo)
                    .titulo(titulo)
                    .descricao("Manutenção de " + plano.getTipo() + " atingiu o limite configurado.")
                    .tipo(com.drivenote.enums.TipoLembrete.MANUTENCAO)
                    .dataAlerta(java.time.LocalDate.now())
                    .status(com.drivenote.enums.StatusLembrete.ATRASADO)
                    .build());
            }
        }
    }
}
```

- [ ] **Step 3: Commit**
```bash
git add backend/src/main/java/com/drivenote/service/LembreteService.java
git commit -m "backend: implement automated maintenance reminders logic"
```

### Task 3: Scheduler and Controller Integration

**Files:**
- Modify: `backend/src/main/java/com/drivenote/scheduler/LembreteScheduler.java`
- Modify: `backend/src/main/java/com/drivenote/controller/LembreteController.java`

- [ ] **Step 1: Update Scheduler**
Call the new method in the daily routine.

- [ ] **Step 2: Add trigger endpoint to Controller**
Add an endpoint for manual/simulation trigger.

```java
// backend/src/main/java/com/drivenote/controller/LembreteController.java
@Operation(summary = "Processar manutenções vencidas manualmente (Simulação)")
@PostMapping("/processar-vencidos")
public void processarVencidos() {
    service.gerarLembretesManutencaoVencida();
}
```

- [ ] **Step 3: Commit**
```bash
git add backend/src/main/java/com/drivenote/scheduler/LembreteScheduler.java backend/src/main/java/com/drivenote/controller/LembreteController.java
git commit -m "backend: integrate automated reminders with scheduler and controller"
```
