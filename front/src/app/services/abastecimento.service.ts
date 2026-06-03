import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AbastecimentoResponse, AbastecimentoCreateRequest } from '../models/abastecimento.model';

@Injectable({
  providedIn: 'root'
})
export class AbastecimentoService {
  private readonly API_URL = `${environment.apiUrl}/abastecimentos`;

  constructor(private http: HttpClient) {}

  listarPorVeiculo(veiculoId: number): Observable<AbastecimentoResponse[]> {
    return this.http.get<AbastecimentoResponse[]>(`${this.API_URL}/veiculo/${veiculoId}`);
  }

  criar(request: AbastecimentoCreateRequest): Observable<AbastecimentoResponse> {
    return this.http.post<AbastecimentoResponse>(this.API_URL, request);
  }
}
