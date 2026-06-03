# Design Spec - Home Page Structure and Integration

Build a functional, responsive Home page for the DriveNote application, integrating with the backend Dashboard API and implementing a sidebar-based layout.

## 1. Component Architecture

The `HomeComponent` will be decomposed into smaller, focused standalone components:

### SidebarComponent (`/components/sidebar`)
- **Logo**: Displays the project logo at the top.
- **Menu Items**: Home, Veiculos, Manutenções, Abastecimentos, Lembretes, Perfil, Configurações.
- **Icons**: Each item will have a representative icon.
- **Collapsible**: Desktop sidebar can be toggled between expanded (full text) and collapsed (icons only) states.
- **Mobile Support**: Hidden by default on mobile, triggered by a Hamburger menu.
- **Logout**: Logout button at the bottom.

### VehicleCardComponent (`/components/vehicle-card`)
- Displays core vehicle info: Name, Model, Year, Plate, Current KM.
- Styled as a prominent card at the top of the main content.

### StatCardComponent (`/components/stat-card`)
- Reusable card for key metrics.
- Used 4 times in a row: Next Maintenance, Autonomy, Last Refuel, Monthly Spend.

### AlertsTableComponent (`/components/alerts-table`)
- Table displaying notifications and alerts (Lembretes).
- **Status Colors**: (Defined in `src/styles/_colors.scss`)
  - PENDENTE: Purple (`$status-pending`)
  - ATRASADO: Red (`$status-delayed`)
  - CONCLUIDO: Green (`$status-completed`)

## 2. Service & Data Integration

### DashboardService (`/services/dashboard.service.ts`)
- Implements `getDashboard(veiculoId: number)` calling `GET /api/dashboard/veiculo/{veiculoId}`.
- Uses `AuthService` (or local storage) to identify the current vehicle/user.

### Models (`/models/dashboard.model.ts`)
- TypeScript interfaces matching `DashboardResponse` DTO.

## 3. UI/UX & Layout

- **Layout Structure**: Sidebar + Main Content container.
- **Responsiveness**:
  - **Desktop**: Sidebar takes dynamic width; main content expands.
  - **Mobile**: Sidebar becomes an overlay triggered by a hamburger menu. Stat cards stack vertically or use a 2x2 grid.
- **Interactivity**:
  - Sidebar toggle (Expand/Collapse).
  - Main action buttons: "Novo Registro" and "Novo Abastecimento" at the top of the content area.

## 4. Implementation Strategy

1. Create models and service.
2. Build sub-components (VehicleCard, StatCard, AlertsTable).
3. Build SidebarComponent with toggle logic.
4. Assemble everything in `HomeComponent`.
5. Implement responsive CSS using Media Queries.

## Success Criteria

- Home page successfully fetches and displays real data from the backend.
- Sidebar collapses/expands correctly on desktop.
- Hamburger menu works on mobile.
- Status colors in the alerts table match the requirements.
- Layout remains usable and visually appealing on all screen sizes.
- **NO NEW AUTOMATED TESTS** (per user request).
