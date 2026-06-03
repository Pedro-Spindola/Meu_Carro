# Automated Maintenance Reminders Design

**Goal:** Automatically generate reminders when a vehicle's maintenance plan becomes overdue (by date or mileage) and provide an endpoint to trigger this check on demand.

## Backend Changes

### Repository
- **LembreteRepository**: Add `existsByVeiculoIdAndTituloAndStatusIn(Long veiculoId, String titulo, Collection<StatusLembrete> statuses)` to avoid duplicate reminders for the same overdue maintenance.

### Service
- **LembreteService**: 
    - Implement `gerarLembretesManutencaoVencida()`:
        1. Fetch all active `PlanoManutencao`.
        2. For each plan, calculate if it's overdue using the logic from `DashboardService` (comparing `intervaloKm` with `veiculo.quilometragemAtual` and `intervaloDias` with `lastManutencao.data`).
        3. If overdue and no pending/overdue reminder exists with the title "Manutenção Vencida: [Tipo]", create a new `Lembrete`.
    - Mark this method as `@Transactional`.

### Scheduler
- **LembreteScheduler**: Add a call to `lembreteService.gerarLembretesManutencaoVencida()` inside the daily task (or a new one).

### Controller (Simulation/Manual Trigger)
- **LembreteController**: Add a `@PostMapping("/processar-vencidos")` endpoint that calls the service method manually.

## Data Logic
- **Overdue by KM**: `veiculo.quilometragemAtual >= (ultimaManutencao.km + plano.intervaloKm)`.
- **Overdue by Date**: `LocalDate.now() >= (ultimaManutencao.data + plano.intervaloDias)`.

## Testing Strategy
- **Integration Test**: Create a vehicle with a plan, simulate it being overdue (by advancing date or increasing KM), call the endpoint, and verify the reminder is created.
