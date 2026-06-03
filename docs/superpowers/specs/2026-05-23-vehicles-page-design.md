# Design Spec - Vehicles Page (List and CRUD)

Implement the "Veículos" page to list, add, and edit user vehicles. The layout will follow a modern list-style design with a dynamic, toggleable form.

## 1. Component Architecture

### VeiculoComponent (`/pages/veiculo`)
- **Main Container**: Manages the list of vehicles and the visibility of the form.
- **Header**: Título "Meus Veículos" and "Novo Veículo" button.
- **List Area**: Loops through vehicles and renders `VeiculoListItemComponent`.
- **Form Area**: Container for `VeiculoFormComponent`, initially hidden using `*ngIf`. Shows when "Novo Veículo" or "Editar" is clicked.

### VeiculoListItemComponent (`/components/veiculo-list-item`)
- Displays: Brand/Model, Plate, KM, and Fuel Type.
- Action: "Editar" button that triggers the edit mode in the parent.

### VeiculoFormComponent (`/components/veiculo-form`)
- Reactive Form with fields: Marca, Modelo, Ano, Placa, Combustível (Select), KM.
- Validations: Matches backend constraints (NotBlank, Size, Min).
- Behavior: "Salvar" button is disabled during request (`isLoading`). Hides itself after a successful save.

## 2. Service & Data Integration

### VeiculoService (`/services/veiculo.service.ts`)
- `listar(usuarioId: number)`: `GET /api/usuarios/{usuarioId}/veiculos`
- `criar(usuarioId: number, veiculo: VeiculoCreateRequest)`: `POST /api/usuarios/{usuarioId}/veiculos`
- `atualizar(usuarioId: number, veiculoId: number, veiculo: VeiculoUpdateRequest)`: `PUT /api/usuarios/{usuarioId}/veiculos/{veiculoId}`

### Models (`/models/veiculo.model.ts`)
- TypeScript interfaces matching `VeiculoResponse`, `VeiculoCreateRequest`, and `VeiculoUpdateRequest`.
- `TipoCombustivel` Enum: GASOLINA, ETANOL, DIESEL, FLEX.

## 3. UI/UX & Layout

- **List Layout**: Vertical containers with rounded corners, box-shadow, and a primary color top-border.
- **Form UI**: Displayed as a modal or a card below the list, with smooth transitions.
- **Responsiveness**: 
  - List items stack labels/values on small screens.
  - Form fields use a single column on mobile.
- **Request Validation**: Disable "Salvar" to prevent duplicate requests.

## 4. Implementation Strategy

1. Create models (`veiculo.model.ts`) and enum.
2. Create `VeiculoService`.
3. Build `VeiculoListItemComponent`.
4. Build `VeiculoFormComponent` with Reactive Forms.
5. Build `VeiculoComponent` (page) to orchestrate everything.
6. Register route `/veiculos` in `app.routes.ts`.

## Success Criteria

- Successfully lists vehicles from the backend.
- Can add a new vehicle via the form.
- Can edit an existing vehicle and update the list.
- Form hides/shows correctly.
- No duplicate requests on save.
- **NO NEW AUTOMATED TESTS.**
