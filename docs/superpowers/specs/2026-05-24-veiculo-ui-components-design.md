# Veiculo UI Components Design

## Goal
Implement `VeiculoListItemComponent` and `VeiculoFormComponent` for vehicle management in the front-end.

## Components

### 1. VeiculoListItemComponent
A specialized component for displaying vehicle details in a list view, focused on clarity and actions.

- **Selector:** `app-veiculo-list-item`
- **Inputs:** `veiculo: VeiculoResponse`
- **Outputs:** `editar: EventEmitter<VeiculoResponse>`
- **Visuals:**
  - Border-top with primary color (`var(--primary-color)`).
  - Rounded corners and shadow.
  - Displays: Marca/Modelo (Title), Ano, Placa, Combustível, KM.
  - "Editar" button to trigger the edit action.

### 2. VeiculoFormComponent
A reactive form for creating and updating vehicle records.

- **Selector:** `app-veiculo-form`
- **Inputs:** `veiculo?: VeiculoResponse` (optional, for editing)
- **Outputs:** `save: EventEmitter<VeiculoCreateRequest | VeiculoUpdateRequest>`, `cancel: EventEmitter<void>`
- **State:** `isLoading: Signal<boolean>`
- **Fields:**
  - `marca`: string, required
  - `modelo`: string, required
  - `ano`: number, required, min 1900
  - `placa`: string, required, pattern for Brazilian plates (simple)
  - `combustivel`: select, required (Enum `TipoCombustivel`)
  - `quilometragemAtual`: number, required, min 0
- **Validation:** Visual feedback for invalid fields.

## Tech Stack
- Angular 19+ (Standalone Components, Signals)
- Reactive Forms
- SCSS for styling, using project variables (`var(--primary-color)`, etc.)

## Architecture
- **Isolation:** Components are self-contained with their own templates and styles.
- **Data Flow:** Uses `@Input` for data passing and `@Output` for actions, following the container/presenter pattern where applicable (though these are mostly functional pieces).
- **Styling:** Adheres to the established dark/light theme using CSS variables.
