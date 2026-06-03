import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { VeiculoResponse, VeiculoCreateRequest, VeiculoUpdateRequest } from '../models/veiculo.model';

@Injectable({
  providedIn: 'root'
})
export class VeiculoService {
  private readonly API_URL = (usuarioId: number) => `${environment.apiUrl}/usuarios/${usuarioId}/veiculos`;

  constructor(private http: HttpClient) {}

  listar(usuarioId: number): Observable<VeiculoResponse[]> {
    return this.http.get<VeiculoResponse[]>(this.API_URL(usuarioId));
  }

  criar(usuarioId: number, veiculo: VeiculoCreateRequest): Observable<VeiculoResponse> {
    return this.http.post<VeiculoResponse>(this.API_URL(usuarioId), veiculo);
  }

  atualizar(usuarioId: number, veiculoId: number, veiculo: VeiculoUpdateRequest): Observable<VeiculoResponse> {
    return this.http.put<VeiculoResponse>(`${this.API_URL(usuarioId)}/${veiculoId}`, veiculo);
  }

  deletar(usuarioId: number, veiculoId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL(usuarioId)}/${veiculoId}`);
  }
}
