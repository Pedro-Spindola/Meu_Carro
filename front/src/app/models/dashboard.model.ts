import { LembreteResponse } from './lembrete.model';

export interface ProximaManutencao {
  tipo: string;
  kmRestante: number;
  dataPrevista: string;
}

export interface Autonomia {
  autonomiaEstimada: number;
  consumoMedio: number;
}

export interface UltimoAbastecimento {
  data: string;
  litros: number;
  valor: number;
}

export interface GastosMes {
  total: number;
  quantidadeAbastecimentos: number;
}

export interface DashboardResponse {
  nomeVeiculo: string;
  modelo: string;
  ano: number;
  placa: string;
  quilometragemAtual: number;
  proximasManutencoes: ProximaManutencao[];
  autonomia: Autonomia;
  ultimoAbastecimento: UltimoAbastecimento;
  gastosMes: GastosMes;
  lembretes: LembreteResponse[];
}
