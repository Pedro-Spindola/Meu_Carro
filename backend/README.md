# DriveNote 🚗💨

**DriveNote** é uma API REST para o gerenciamento automotivo pessoal. O sistema permite que proprietários de veículos tenham controle sobre gastos, histórico de manutenções e, principalmente, previsibilidade sobre a saúde do veículo através de um dashboard inteligente.

---

## 🌟 Diferenciais do Projeto
- **Algoritmo de Manutenção Crítica:** O sistema calcula automaticamente a próxima revisão comparando a quilometragem atual versus o tempo decorrido, priorizando o que estiver mais próximo do vencimento.
- **Arquitetura Limpa:** Organização seguindo padrões de mercado, facilitando a escalabilidade e manutenção.
- **Segurança:** Autenticação via JWT (JSON Web Token) com controle de acesso.

---

## 🚀 Funcionalidades Principais
- [x] **Gestão de Veículos:** Cadastro de múltiplos veículos por usuário.
- [x] **Controle de Abastecimento:** Histórico de consumo e média de gastos.
- [x] **Monitoramento de Manutenção:** Registro de serviços realizados e planos futuros.
- [x] **Dashboard Inteligente:** Visualização rápida de alertas críticos e manutenções pendentes.
- [x] **Lembretes Automáticos:** Notificações baseadas em prazos e quilometragem.

---

## 🛠 Tecnologias Utilizadas
- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.3.2
- **Segurança:** Spring Security + JWT
- **Persistência:** Spring Data JPA + Hibernate
- **Bancos de Dados:** PostgreSQL (Produção/Local) e H2 (Testes)
- **Documentação:** Swagger/OpenAPI 3
- **Gestão de Dependências:** Maven

---

## 📦 Estrutura do Projeto
O projeto segue uma estrutura baseada em camadas para separação de responsabilidades:
```text
com.drivenote
 ├─ config      # Configurações do Spring (Swagger, Beans)
 ├─ controller  # Endpoints da API
 ├─ dto         # Objetos de Transferência de Dados (Data Transfer Objects)
 ├─ entity      # Entidades do Banco de Dados (JPA)
 ├─ enums       # Tipos enumerados (Status, Tipo Veículo)
 ├─ exception   # Tratamento global de erros
 ├─ mapper      # Conversão entre Entity e DTO (MapStruct)
 ├─ repository  # Interfaces de comunicação com o banco
 ├─ scheduler   # Tarefas agendadas (verificação de prazos)
 ├─ security    # Filtros e configurações de segurança JWT
 └─ service     # Regras de negócio e lógica do sistema
```
---

## 🔧 Como Executar
Pré-requisitos

JDK 21 instalado.

Maven 3.x.

PostgreSQL rodando localmente.

Passo a Passo

Clonar o repositório:

Bash
git clone [https://github.com/Pedro-Spindola/Meu_Carro.git](https://github.com/Pedro-Spindola/Meu_Carro.git)

cd drivenote/backend

Configurar o Banco de Dados:
Crie um banco de dados chamado drivenote e configure suas credenciais no arquivo src/main/resources/application-local.yml.

**Executar a aplicação:**

Bash
mvn spring-boot:run -Dspring-boot.run.profiles=local

Documentação da API:
Com a aplicação rodando, acesse: http://localhost:8081/swagger-ui/index.html

## 🧪 Testes
Para garantir a qualidade e integridade das regras de negócio:

Bash
mvn test
Os testes utilizam o profile test com banco H2 em memória.

## Health Check
Endpoint para monitoramento:

```
GET /actuator/health
```

## ✒️ Desenvolvedores
**Ozeias Campos**

**Pedro Henrique**

**Henrique Carvalho**