import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LembreteResponse } from '../models/lembrete.model';

@Injectable({
  providedIn: 'root'
})
export class LembreteService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/lembretes`;

  listarPorUsuario(usuarioId: number): Observable<LembreteResponse[]> {
    return this.http.get<LembreteResponse[]>(`${this.API_URL}/usuario/${usuarioId}`);
  }

  concluir(id: number): Observable<LembreteResponse> {
    return this.http.put<LembreteResponse>(`${this.API_URL}/${id}/concluir`, {});
  }

  processarVencidos(): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/processar-planos`, {});
  }
}
