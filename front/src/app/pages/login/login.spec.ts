import { TestBed } from '@angular/core/testing';
import { LoginComponent } from './login';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { signal } from '@angular/core';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let authServiceSpy: any;
  let routerSpy: any;

  beforeEach(async () => {
    authServiceSpy = {
      login: vi.fn()
    };
    routerSpy = {
      navigate: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LoginComponent, ReactiveFormsModule],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    const fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to /home on successful login', () => {
    const mockResponse = { token: 'mock-token' };
    authServiceSpy.login.mockReturnValue(of(mockResponse));
    
    component.loginForm.setValue({
      email: 'test@example.com',
      senha: 'password123'
    });

    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      senha: 'password123'
    });
    expect(localStorage.getItem('token')).toBe('mock-token');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/home']);
    expect(component.isLoading()).toBe(false);
  });

  it('should show error message on failed login', () => {
    authServiceSpy.login.mockReturnValue(throwError(() => new Error('Invalid credentials')));
    
    component.loginForm.setValue({
      email: 'wrong@example.com',
      senha: 'wrongpassword'
    });

    component.onSubmit();

    expect(component.errorMessage()).toBe('Email ou senha inválidos.');
    expect(component.isLoading()).toBe(false);
  });
});
