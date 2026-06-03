export enum TipoManutencao {
  OLEO_MOTOR = 'OLEO_MOTOR',
  FILTRO_OLEO = 'FILTRO_OLEO',
  FILTRO_AR = 'FILTRO_AR',
  FILTRO_COMBUSTIVEL = 'FILTRO_COMBUSTIVEL',
  PNEU = 'PNEU',
  FREIO = 'FREIO',
  BATERIA = 'BATERIA',
  ALINHAMENTO = 'ALINHAMENTO',
  BALANCEAMENTO = 'BALANCEAMENTO',
  SUSPENSAO = 'SUSPENSAO',
  CORREIA_DENTADA = 'CORREIA_DENTADA',
  REVISAO = 'REVISAO',
  OUTRO = 'OUTRO'
}

export interface PlanoManutencaoResponse {
  id: number;
  veiculoId: number;
  tipo: TipoManutencao;
  descricao: string;
  intervaloKm?: number;
  intervaloDias?: number;
  ativo: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface PlanoManutencaoCreateRequest {
  veiculoId: number;
  tipo: TipoManutencao;
  descricao?: string;
  intervaloKm?: number;
  intervaloDias?: number;
  ativo?: boolean;
}
