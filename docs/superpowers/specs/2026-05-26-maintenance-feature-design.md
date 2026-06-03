# Spec: Funcionalidade de Manutenção (Prevention & History)

Este documento descreve o design e a implementação da funcionalidade de Manutenção, que combina o gerenciamento de planos preventivos e o histórico de execuções.

## 1. Visão Geral
A página de Manutenção permitirá ao usuário:
1.  **Gerenciar Planos Preventivos**: Visualizar cards para todos os tipos de manutenção e configurar intervalos de tempo/quilometragem.
2.  **Registrar Histórico**: Lançar manutenções realizadas e visualizar o histórico completo por veículo.

## 2. Arquitetura e Componentes (Frontend)

### 2.1 Modelos (`manutencao.model.ts` & `plano-manutencao.model.ts`)
- `TipoManutencao`: Enum correspondente ao backend.
- `PlanoManutencaoResponse` / `CreateRequest`.
- `ManutencaoResponse` / `CreateRequest`.

### 2.2 Serviços
- `PlanoManutencaoService`: 
    - `listar(veiculoId)`
    - `criar(request)` (ou atualizar, dependendo da lógica de negócio do backend que costuma usar o mesmo endpoint ou similar).
- `ManutencaoService`:
    - `listarPorVeiculo(veiculoId)`
    - `criar(request)`

### 2.3 Componentes de UI
- `ManutencaoPageComponent`: Página principal com seletor de veículo e as duas seções.
- `PlanoCardComponent`: Exibe o status preventivo de um tipo específico (Óleo, Freio, etc).
- `PlanoFormComponent` (Modal): Formulário para configurar intervalos de KM e Dias.
- `ManutencaoFormComponent` (Modal): Formulário para registrar uma manutenção realizada.
- `ManutencaoTableComponent`: Lista o histórico de manutenções.

## 3. Lógica de Status (Cálculo de Notificação)
Para cada Card de Prevenção, o frontend calculará o status comparando a KM/Data do último registro com o intervalo do plano:
- **Status OK (Verde)**: < 90% do intervalo percorrido.
- **Atenção (Amarelo)**: > 90% do intervalo percorrido ou faltando < 15 dias.
- **Vencido (Vermelho)**: KM ou Data atual ultrapassou o planejado.
- **Sem Plano (Cinza)**: Nenhum plano ativo para aquele tipo.

## 4. Fluxo do Usuário
1.  O usuário seleciona um veículo no topo da página.
2.  A seção superior carrega cards para **todos** os tipos do `TipoManutencao`.
    - Se houver plano, mostra o progresso (ex: "Faltam 2.000 km").
    - Se não houver, mostra "Plano inativo".
3.  A seção inferior mostra a tabela de manutenções já realizadas para aquele veículo.
4.  O usuário pode clicar em "Novo Registro" para lançar uma manutenção, ou clicar em "Gerenciar" em um card para configurar a regra preventiva.

## 5. UI/UX e Estilo
- **Grid de Cards**: Layout responsivo (1 card por linha no mobile, 3-4 no desktop).
- **Modais**: Uso do componente de modal padrão do projeto para manter a consistência.
- **Cores Semânticas**: Uso rigoroso de Verde/Amarelo/Vermelho para indicar a urgência das manutenções.
