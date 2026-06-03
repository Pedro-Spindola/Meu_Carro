export interface ManutencaoResponse {
  id: number;
  veiculoId: number;
  planoId?: number;
  descricao: string;
  data: string;
  quilometragem: number;
  valor: number;
  createdAt: string;
}

export interface ManutencaoCreateRequest {
  veiculoId: number;
  planoId?: number;
  descricao: string;
  data: string;
  quilometragem: number;
  valor: number;
}
