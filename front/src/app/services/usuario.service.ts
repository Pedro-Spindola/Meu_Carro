import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioResponse, UsuarioUpdateRequest, SenhaUpdateRequest } from '../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/usuarios`;

  buscarPorId(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.API_URL}/${id}`);
  }

  atualizar(id: number, request: UsuarioUpdateRequest): Observable<UsuarioResponse> {
    return this.http.put<UsuarioResponse>(`${this.API_URL}/${id}`, request);
  }

  alterarSenha(id: number, request: SenhaUpdateRequest): Observable<void> {
    return this.http.put<void>(`${this.API_URL}/${id}/senha`, request);
  }
}
