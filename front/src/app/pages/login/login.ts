import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = signal(false);
  errorMessage = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    public themeService: ThemeService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.loginForm.invalid || this.isLoading()) return;

    this.isLoading.set(true);
    this.errorMessage.set('');

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        console.log('Login response:', res);
        if (res.token && res.usuarioId) {
          localStorage.setItem('token', res.token);
          localStorage.setItem(this.authService.ID_USER, res.usuarioId.toString());
          this.router.navigate(['/home']);
        } else {
          this.errorMessage.set('Resposta do servidor incompleta. Verifique se o backend está atualizado.');
        }
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Email ou senha inválidos.');
        this.isLoading.set(false);
      }
    });
  }
}
