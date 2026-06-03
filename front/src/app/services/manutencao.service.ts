import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ManutencaoResponse, ManutencaoCreateRequest } from '../models/manutencao.model';

@Injectable({
  providedIn: 'root'
})
export class ManutencaoService {
  private readonly API_URL = `${environment.apiUrl}/manutencoes`;

  constructor(private http: HttpClient) {}

  criar(request: ManutencaoCreateRequest): Observable<ManutencaoResponse> {
    return this.http.post<ManutencaoResponse>(this.API_URL, request);
  }

  listarPorVeiculo(veiculoId: number): Observable<ManutencaoResponse[]> {
    return this.http.get<ManutencaoResponse[]>(`${this.API_URL}/veiculo/${veiculoId}`);
  }

  listarPorPlano(planoId: number): Observable<ManutencaoResponse[]> {
    return this.http.get<ManutencaoResponse[]>(`${this.API_URL}/plano/${planoId}`);
  }
}
