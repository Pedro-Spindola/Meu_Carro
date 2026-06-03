export interface UsuarioResponse {
  id: number;
  nome: string;
  email: string;
  telefone: string;
  createdAt: string;
  updatedAt: string;
}

export interface UsuarioUpdateRequest {
  nome: string;
  telefone: string;
}

export interface SenhaUpdateRequest {
  senhaAtual: string;
  novaSenha: string;
}
