import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { PlanoManutencaoResponse, PlanoManutencaoCreateRequest } from '../models/plano-manutencao.model';

@Injectable({
  providedIn: 'root'
})
export class PlanoManutencaoService {
  private readonly API_URL = `${environment.apiUrl}/planos-manutencao`;

  constructor(private http: HttpClient) {}

  criar(request: PlanoManutencaoCreateRequest): Observable<PlanoManutencaoResponse> {
    return this.http.post<PlanoManutencaoResponse>(this.API_URL, request);
  }

  listarPorVeiculo(veiculoId: number): Observable<PlanoManutencaoResponse[]> {
    return this.http.get<PlanoManutencaoResponse[]>(`${this.API_URL}/veiculo/${veiculoId}`);
  }

  atualizar(id: number, request: PlanoManutencaoCreateRequest): Observable<PlanoManutencaoResponse> {
    return this.http.put<PlanoManutencaoResponse>(`${this.API_URL}/${id}`, request);
  }
}
