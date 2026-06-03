# Reminders (Lembrete) Feature Design

**Goal:** Implement a global reminder system that displays alerts for all user vehicles on the Home page, with the ability to view details and complete them directly.

## Backend Changes

### DTOs
- **LembreteResponse**: Add `nomeVeiculo` (String) and `placaVeiculo` (String) fields to provide context when viewing global reminders.
- **Enums**: Ensure `StatusLembrete` and `TipoLembrete` are aligned with the frontend.

### Repository
- **LembreteRepository**: Add `findAllByVeiculoUsuarioIdOrderByDataAlertaAsc(Long usuarioId)` to fetch all active reminders for a user.

### Service
- **LembreteService**: Update `toResponse` mapper logic to populate vehicle information.
- Implement `listarPorUsuario(Long usuarioId)`.

### Controller
- **LembreteController**: Add `@GetMapping("/usuario/{usuarioId}")` endpoint.

## Frontend Changes

### Models
- Create `front/src/app/models/lembrete.model.ts` with interfaces for `LembreteResponse`, `StatusLembrete`, and `TipoLembrete`.

### Services
- Create `front/src/app/services/lembrete.service.ts` to handle API calls for global reminders and the "concluir" action.

### UI Components
- **Sidebar**: Remove "Lembretes" link.
- **Home Page**:
    - Load global reminders using the new user-based endpoint.
    - Replace vehicle-specific dashboard reminders with the global list.
- **Alerts Table (`app-alerts-table`)**:
    - Add a "Concluir" button (check icon) to each row.
    - Show vehicle context (e.g., "Civic - IPVA").
    - Open a detail modal when clicking a row.
- **Reminder Detail Modal**:
    - New component to show all fields: Title, Description, Type, Date, Status, and Vehicle.

## Data Flow
1. Home page initializes -> fetches user's vehicles AND all user's reminders.
2. `AlertsTable` receives the global list.
3. User clicks "Concluir" -> calls `LembreteService.concluir(id)` -> refreshes the list.
4. User clicks a row -> opens `ReminderDetailModal`.

## Testing Strategy
- **Backend**: Unit test for the new repository method and controller endpoint.
- **Frontend**: Verify that the global list is correctly populated and the "concluir" action updates the UI state.
