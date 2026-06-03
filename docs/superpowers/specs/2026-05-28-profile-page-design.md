# Profile Page Design

**Goal:** Implement a Profile page where users can view/edit personal data, change passwords, and configure application preferences (default vehicle and theme).

## Backend Changes

### DTOs
- Create `SenhaUpdateRequest`:
    ```java
    public record SenhaUpdateRequest(
        @NotBlank String senhaAtual,
        @NotBlank @Size(min = 6) String novaSenha
    ) {}
    ```

### Controller (`UsuarioController`)
- Add `GET /api/usuarios/{id}` to fetch current user data.
- Add `PUT /api/usuarios/{id}` to update name and phone (using `UsuarioUpdateRequest`).
- Add `PUT /api/usuarios/{id}/senha` to update password.

### Service (`UsuarioService`)
- Implement `alterarSenha(Long id, SenhaUpdateRequest request)` with current password verification.

## Frontend Changes

### Models
- Update `UsuarioResponse` to match backend.
- Create `SenhaUpdateRequest` interface.

### Services
- **`UsuarioService`**: Add methods for fetching and updating user data/password.
- **`ThemeService`**: Ensure theme preference can be persisted (e.g., in `localStorage`).
- **`VeiculoService`**: Add a mechanism to set/get the "Default Vehicle" for the Home page (persisted in `localStorage`).

### UI Components
- **`ProfilePage`**:
    - **Personal Data Section**: Reactive form with Name and Phone.
    - **Security Section**: Form with Current Password, New Password, and Confirm Password.
    - **Preferences Section**:
        - Dropdown to select "Default Vehicle" (from user's vehicle list).
        - Toggle/Buttons for Light/Dark mode (integrated with `ThemeService`).

### Navigation
- Add "Perfil" option to the `Sidebar` or a header menu (if exists).

## Integration
1. User logs in -> Home shows.
2. User goes to Profile -> Page fetches `UsuarioResponse`.
3. User changes theme -> `ThemeService` updates immediately and saves to `localStorage`.
4. User selects default vehicle -> Saved to `localStorage`.
5. On Home page load, if a default vehicle ID exists in `localStorage`, it is prioritized.

## Testing Strategy
- **Backend**: Test password update (correct vs incorrect current password).
- **Frontend**: Verify form validations and theme switching persistence.
