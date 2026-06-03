export type TipoLembrete = 'MANUTENCAO' | 'DOCUMENTO' | 'SEGURO' | 'IPVA' | 'LICENCIAMENTO' | 'VIAGEM' | 'OUTRO';
export type StatusLembrete = 'PENDENTE' | 'CONCLUIDO' | 'ATRASADO';

export interface LembreteResponse {
  id: number;
  veiculoId: number;
  nomeVeiculo: string;
  placaVeiculo: string;
  titulo: string;
  descricao: string;
  tipo: TipoLembrete;
  dataAlerta: string;
  status: StatusLembrete;
  createdAt: string;
}
