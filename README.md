
# Sistema de Gerenciamento Hospitalar

Um backend modular e seguro para gerenciamento hospitalar desenvolvido com Java 21, Spring Boot e GraphQL.

## Visão Geral

Este sistema foi projetado para gerenciar as operações básicas de um hospital, com foco em segurança, escalabilidade e comunicação assíncrona. O sistema suporta três tipos de usuários com diferentes níveis de acesso:

- **Médicos**: Podem visualizar e editar o histórico completo de consultas
- **Enfermeiros**: Podem registrar consultas e acessar o histórico
- **Pacientes**: Podem visualizar apenas suas próprias consultas

## Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.4** - Framework principal
- **Spring Security** - Autenticação e autorização
- **GraphQL** - API para consultas flexíveis
- **JWT** - Tokens de autenticação
- **PostgreSQL** - Banco de dados principal
- **RabbitMQ** - Mensageria assíncrona
- **Docker** - Containerização
- **Maven** - Gerenciamento de dependências

## Funcionalidades

### Implementadas e Funcionando

#### Autenticação e Autorização
- Login com JWT
- Controle de acesso baseado em roles (RBAC)
- Senhas criptografadas com BCrypt
- Tokens com expiração configurável

#### Gestão de Usuários
- **Cadastro de médicos** via GraphQL
- **Cadastro de enfermeiros** via GraphQL
- **Cadastro de pacientes** via GraphQL
- Validação de dados únicos (CRM, COREN, CPF, Email)
- Busca e listagem de usuários por tipo
- Dados de exemplo pré-carregados

#### Gestão de Consultas
- Criação de consultas (enfermeiros e médicos)
- Edição de consultas (apenas médicos)
- Visualização com controle de acesso por role
- Histórico completo de consultas
- Busca por período e filtros

#### Mensageria Assíncrona
- **RabbitMQ** integrado e funcionando
- **Eventos de consulta** publicados automaticamente
- **Filas configuradas** para processamento assíncrono
- **Listeners ativos** processando mensagens

#### Context Path
- **Todos os endpoints** usam o prefixo `/api`
- **GraphQL**: `http://localhost:8080/api/graphql`
- **GraphiQL**: `http://localhost:8080/api/graphiql`


## Arquitetura

```
src/main/java/com/fiap/atividade3/
├── config/                 # Configurações (Security, GraphQL, Async)
├── graphql/               # Resolvers e tipos GraphQL
├── model/                 # Entidades e enums
├── repository/            # Repositórios JPA
├── service/               # Lógica de negócio
└── security/              # Componentes de segurança JWT
```

## Modelo de Dados

### Entidades Principais
- **Usuario** - Entidade base para todos os usuários
- **Medico** - Herda de Usuario, possui CRM e especialidade
- **Enfermeiro** - Herda de Usuario, possui COREN e setor
- **Paciente** - Herda de Usuario, possui CPF e dados pessoais
- **Consulta** - Registros de consultas médicas

### Relacionamentos
- Paciente ↔ Consultas (1:N)
- Médico ↔ Consultas (1:N)
- Enfermeiro ↔ Consultas (1:N)

## Como Executar

### Opção 1: Docker (Recomendado)

#### Pré-requisitos
- Docker
- Docker Compose

#### Passos
1. **Clone o repositório**
   ```bash
   git clone <repository-url>
   cd hospital-management-syste
   ```

2. **Execute com Docker**
   ```bash
   ./run-docker.sh start
   ```

3. **Acesse a aplicação**
   - **GraphiQL**: `http://localhost:8080/api/graphiql`
   - **RabbitMQ Management**: `http://localhost:15672` (hospital/hospital123)
   - **Health Check**: `http://localhost:8080/api/actuator/health`

### Opção 2: Execução Local

#### Pré-requisitos
- Java 21+
- Maven 3.6+
- PostgreSQL
- RabbitMQ

#### Passos

1. **Clone o repositório**
   ```bash
   git clone <repository-url>
   cd hospital-management-syste
   ```

2. **Compile o projeto**
   ```bash
   mvn clean compile
   ```

3. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse a aplicação**
   - **GraphiQL (API Principal)**: `http://localhost:8080/api/graphiql`


## Como Testar a API

### GraphQL (Recomendado)

**URL**: `http://localhost:8080/api/graphiql`

#### Explorando os Endpoints Disponíveis

1. **Abra o GraphiQL** no seu navegador
2. **Clique em "Docs"** (canto superior direito) para ver todos os endpoints
3. **Use auto-complete**: Digite `query {` ou `mutation {` e pressione `Ctrl+Space`

#### Exemplos Práticos de Teste

##### 1. Login (OBRIGATÓRIO PRIMEIRO)
```graphql
mutation {
  login(input: {
    email: "joao.silva@hospital.com"
    senha: "senha123"
  }) {
    token
    usuario {
      nome
      role
    }
  }
}
```

**IMPORTANTE**: Após fazer login, copie o `token` e configure no cabeçalho:
```json
{
  "Authorization": "Bearer SEU_TOKEN_AQUI"
}
```

**Usuários de Teste Disponíveis:**
- **Médicos**: `joao.silva@hospital.com`, `maria.santos@hospital.com`
- **Enfermeiros**: `ana.costa@hospital.com`, `carlos.oliveira@hospital.com`  
- **Pacientes**: `jose.pereira@email.com`, `maria.fernanda@email.com`
- **Senha padrão**: `senha123`

##### 2. Listar Médicos (Requer Token)
```graphql
query {
  medicos {
    id
    nome
    email
    crm
    especialidade
    active
    createdAt
  }
}
```

##### 3. Cadastrar um Novo Médico (Requer Token)
```graphql
mutation {
  registrarMedico(input: {
    nome: "Dr. Pedro Almeida"
    email: "pedro.almeida@hospital.com"
    senha: "senha123"
    crm: "54321-SP"
    especialidade: "Neurologia"
  }) {
    id
    nome
    email
    crm
    especialidade
    role
    active
    createdAt
  }
}
```

##### 3. Cadastrar um Novo Enfermeiro
```graphql
mutation {
  registrarEnfermeiro(input: {
    nome: "Fernanda Lima"
    email: "fernanda.lima@hospital.com"
    senha: "senha123"
    coren: "789012-SP"
    setor: "Pediatria"
  }) {
    id
    nome
    email
    coren
    setor
    role
    active
    createdAt
  }
}
```

##### 4. Cadastrar um Novo Paciente (Requer Token)
```graphql
mutation {
  registrarPaciente(input: {
    nome: "Roberto Silva"
    email: "roberto.silva@email.com"
    senha: "senha123"
    cpf: "987.654.321-00"
    dataNascimento: "1990-03-20"
    telefone: "(11) 88888-8888"
    endereco: "Av. Paulista, 1000"
  }) {
    id
    nome
    email
    cpf
    telefone
    dataNascimento
    endereco
    role
  }
}
```

##### 5. Listar Médicos (Requer Token)
```graphql
query {
  medicos {
    id
    nome
    email
    crm
    especialidade
    active
  }
}
```

##### 7. Listar Consultas
```graphql
query {
  consultas {
    id
    motivo
    diagnostico
    dataConsulta
    medico {
      nome
      especialidade
    }
    paciente {
      nome
    }
    enfermeiro {
      nome
    }
  }
}
```

### Testes de Validação Completos

#### Validações que Você Pode Testar

1. **Email Duplicado**: Tente cadastrar dois usuários com o mesmo email
2. **CRM Duplicado**: Tente cadastrar dois médicos com o mesmo CRM
3. **COREN Duplicado**: Tente cadastrar dois enfermeiros com o mesmo COREN
4. **CPF Duplicado**: Tente cadastrar dois pacientes com o mesmo CPF
5. **Campos Obrigatórios**: Tente cadastrar sem preencher campos obrigatórios

#### Testes de RabbitMQ e Messaging Assíncrono

##### 1. Teste de Criação de Consulta com Messaging
```bash
# Via curl (após obter token via GraphQL)
curl -X POST http://localhost:8080/api/graphql \
-H "Content-Type: application/json" \
-H "Authorization: Bearer SEU_TOKEN_AQUI" \
-d '{
  "query": "mutation { criarConsulta(input: { pacienteId: 5, medicoId: 2, dataConsulta: \"2025-01-16T14:30:00\", motivo: \"Teste RabbitMQ\", observacoes: \"Validando messaging assíncrono\" }) { id dataConsulta motivo paciente { nome } medico { nome } } }"
}'
```

##### 2. Verificar Logs de Messaging
```bash
# Verificar logs da aplicação
docker logs hospital-app | grep -E "Publishing|Received|consultation"

# Exemplo de log esperado:
# 2025-09-09 00:03:26 - Publishing consultation created event for consultation ID: 4
```

##### 3. Verificar Status das Filas RabbitMQ
```bash
# Via API do RabbitMQ Management
curl -u hospital:hospital123 http://localhost:15672/api/queues

# Ou acesse: http://localhost:15672 (hospital/hospital123)
```

##### 4. Verificar Health Check Completo
```bash
curl -s http://localhost:8080/api/actuator/health

# Deve retornar status UP para:
# - database (PostgreSQL)
# - rabbit (RabbitMQ)
# - diskSpace
```

#### Dicas de Uso do GraphiQL

- **Auto-complete**: `Ctrl+Space` (Windows/Linux) ou `Cmd+Space` (Mac)
- **Executar Query**: `Ctrl+Enter` ou clique no botão
- **Formatar Código**: `Ctrl+Shift+P`
- **Histórico**: Seta para cima/baixo para navegar no histórico

### API 100% GraphQL

**Arquitetura moderna e unificada**

- **GraphiQL é a única interface** necessária para testar a API
- **Documentação automática** integrada no GraphiQL
- **Queries flexíveis** - você escolhe exatamente os campos que precisa

**Vantagens do GraphQL:**
- **API única e consistente**
- **Menos requisições** (busca dados relacionados em uma query)
- **Tipagem forte** com validação automática
- **Documentação automática** no GraphiQL
- **Melhor experiência de desenvolvimento**

## Dados de Exemplo

A aplicação carrega automaticamente dados de exemplo na inicialização:

### Médicos
- **Dr. João Silva** - `joao.silva@hospital.com` - CRM: 12345-SP - Cardiologia
- **Dra. Maria Santos** - `maria.santos@hospital.com` - CRM: 67890-SP - Pediatria

### Enfermeiros
- **Ana Costa** - `ana.costa@hospital.com` - COREN: 123456-SP - UTI
- **Carlos Oliveira** - `carlos.oliveira@hospital.com` - COREN: 654321-SP - Emergência

### Pacientes
- **José Pereira** - `jose.pereira@email.com` - CPF: 123.456.789-01
- **Maria Fernanda** - `maria.fernanda@email.com` - CPF: 987.654.321-09

### Credenciais
- **Senha padrão para todos**: `senha123`
- **Consultas de exemplo**: 2 consultas já cadastradas

### Como Testar com Dados Existentes

1. **Use o login do exemplo 1** com qualquer usuário de teste
2. **Liste os dados existentes**:
```graphql
query {
  medicos {
    id
    nome
    email
    crm
    especialidade
  }
}
```

## Segurança

### Controle de Acesso por Role

| Operação | Médico | Enfermeiro | Paciente |
|----------|--------|------------|----------|
| Visualizar todas consultas | Sim | Sim | Não |
| Visualizar próprias consultas | Sim | Sim | Sim |
| Criar consulta | Sim | Sim | Não |
| Editar consulta | Sim | Não | Não |
| Deletar consulta | Sim | Não | Não |
| Buscar usuários | Sim | Sim | Não |

### Headers de Autenticação
```
Authorization: Bearer <JWT_TOKEN>
```

## Mensageria Assíncrona

O sistema utiliza RabbitMQ para comunicação assíncrona:

- **Exchange**: `consulta.exchange`
- **Filas**: 
  - `consulta.created.queue` - Consultas criadas
  - `consulta.updated.queue` - Consultas atualizadas

### Status Verificado
- **RabbitMQ funcionando**: Mensagens sendo publicadas e processadas
- **Listeners ativos**: Processamento assíncrono funcionando
- **Management UI**: Disponível em http://localhost:15672

## Testes

Para executar os testes:
```bash
mvn test
```

## Configurações

### Docker Compose
- **PostgreSQL**: Banco de dados principal
- **RabbitMQ**: Messaging assíncrono com Management UI
- **Redis**: Cache (opcional)
- **Aplicação**: Container principal

### Variáveis de Ambiente
- `JWT_SECRET`: Chave secreta para JWT
- `JWT_EXPIRATION`: Tempo de expiração do token (ms)
- `SPRING_RABBITMQ_HOST`: Host do RabbitMQ
- `SPRING_RABBITMQ_PORT`: Porta do RabbitMQ
- `SPRING_DATASOURCE_URL`: URL do PostgreSQL

## Deploy

Para deploy em produção:

1. Configure um banco de dados PostgreSQL/MySQL
2. Configure RabbitMQ em cluster
3. Ajuste as configurações de segurança
4. Configure HTTPS
5. Implemente monitoramento e logs

## Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [GraphQL Java Documentation](https://www.graphql-java.com/)
- [Spring Security Documentation](https://spring.io/projects/spring-security)

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
