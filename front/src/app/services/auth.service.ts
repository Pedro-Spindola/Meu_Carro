import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthRequest, AuthResponse } from '../models/auth.model';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = `${environment.apiUrl}/auth`;
  public readonly ID_USER = 'usuarioId';

  constructor(private http: HttpClient) {}

  getUsuarioId(): number | null {
    const id = localStorage.getItem(this.ID_USER);
    return id ? parseInt(id, 10) : null;
  }

  login(request: AuthRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, request);
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/logout`, {});
  }
}
