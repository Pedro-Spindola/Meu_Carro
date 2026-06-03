export enum TipoCombustivel {
  GASOLINA = 'GASOLINA',
  ETANOL = 'ETANOL',
  DIESEL = 'DIESEL',
  FLEX = 'FLEX'
}

export interface VeiculoResponse {
  id: number;
  marca: string;
  modelo: string;
  ano: number;
  placa: string;
  combustivel: TipoCombustivel;
  quilometragemAtual: number;
}

export interface VeiculoCreateRequest {
  marca: string;
  modelo: string;
  ano: number;
  placa: string;
  combustivel: TipoCombustivel;
  quilometragemAtual: number;
}

export type VeiculoUpdateRequest = VeiculoCreateRequest;
