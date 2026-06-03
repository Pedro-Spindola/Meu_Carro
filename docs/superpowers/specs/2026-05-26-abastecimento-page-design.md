# Spec: Página de Abastecimentos (Fuel Refills)

Este documento descreve o design e a implementação da página de Abastecimentos para o sistema DriveNote. A página permitirá ao usuário gerenciar o histórico de combustível de seus veículos, visualizando métricas de desempenho calculadas pelo backend.

## 1. Visão Geral
A página de Abastecimentos será acessível pelo menu lateral e oferecerá uma visão consolidada dos gastos com combustível. O usuário poderá filtrar o histórico por veículo e cadastrar novos abastecimentos.

## 2. Arquitetura e Componentes (Frontend)

### 2.1 Modelos e Interfaces (`abastecimento.model.ts`)
Baseado nos DTOs do backend:
- `AbastecimentoResponse`: Inclui métricas como `consumoMedio` e `custoPorKm`.
- `AbastecimentoCreateRequest`: Dados necessários para o POST.

### 2.2 Serviço (`abastecimento.service.ts`)
Endpoints a serem implementados:
- `POST /api/abastecimentos`: Criar novo registro.
- `GET /api/abastecimentos/veiculo/{veiculoId}`: Listar por veículo.

### 2.3 Componentes de UI
- `AbastecimentoComponent` (Página): Gerencia o estado da página (veículo selecionado, lista de registros).
- `AbastecimentoFormComponent`: Formulário reutilizável para criar registros.
- `AbastecimentoTableComponent`: Tabela estilizada para exibir o histórico, destacando as métricas de performance.

## 3. Fluxo do Usuário

1. **Seleção de Veículo**: Ao entrar na página, o usuário verá um dropdown com seus veículos.
2. **Visualização**: Ao selecionar um veículo, a lista de abastecimentos é carregada dinamicamente.
3. **Cadastro**: 
   - O usuário clica em "Novo Abastecimento".
   - Um formulário é exibido. Se um veículo já estiver selecionado no filtro da página, ele virá pré-selecionado no formulário.
   - Após salvar, a lista é atualizada.

## 4. UI/UX e Estilo
- **Métricas em Destaque**: O consumo médio e custo por KM devem ter destaque visual (badges ou cores diferentes) para facilitar a leitura.
- **Responsividade**: A tabela deve se transformar em cards em dispositivos móveis.
- **Feedback**: Loading states durante as requisições e mensagens de sucesso/erro.

## 5. Critérios de Aceite
- O usuário deve conseguir selecionar qualquer um de seus veículos.
- Os cálculos de consumo retornados pelo backend devem ser exibidos corretamente.
- O formulário deve validar campos obrigatórios (valor, litros, KM).
