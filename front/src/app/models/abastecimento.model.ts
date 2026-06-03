import { TipoCombustivel } from './veiculo.model';

export interface AbastecimentoResponse {
  id: number;
  veiculoId: number;
  data: string;
  tipoCombustivel: TipoCombustivel;
  valorTotal: number;
  litros: number;
  valorLitro: number;
  quilometragem: number;
  consumoMedio: number;
  custoPorKm: number;
  createdAt: string;
}

export interface AbastecimentoCreateRequest {
  veiculoId: number;
  data: string;
  tipoCombustivel: TipoCombustivel;
  valorTotal: number;
  litros: number;
  valorLitro?: number;
  quilometragem: number;
}
