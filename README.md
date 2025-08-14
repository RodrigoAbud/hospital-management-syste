# Sistema de Gerenciamento Hospitalar

Um backend modular e seguro para gerenciamento hospitalar desenvolvido com Java 21, Spring Boot e GraphQL.

## 🏥 Visão Geral

Este sistema foi projetado para gerenciar as operações básicas de um hospital, com foco em segurança, escalabilidade e comunicação assíncrona. O sistema suporta três tipos de usuários com diferentes níveis de acesso:

- **Médicos**: Podem visualizar e editar o histórico completo de consultas
- **Enfermeiros**: Podem registrar consultas e acessar o histórico
- **Pacientes**: Podem visualizar apenas suas próprias consultas

## 🚀 Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.4** - Framework principal
- **Spring Security** - Autenticação e autorização
- **GraphQL** - API para consultas flexíveis
- **JWT** - Tokens de autenticação
- **H2 Database** - Banco de dados em memória (desenvolvimento)
- **RabbitMQ** - Mensageria assíncrona
- **Maven** - Gerenciamento de dependências

## 📋 Funcionalidades

### Autenticação e Autorização
- Login com JWT
- Controle de acesso baseado em roles (RBAC)
- Senhas criptografadas com BCrypt
- Tokens com expiração configurável

### Gestão de Usuários
- Cadastro de médicos, enfermeiros e pacientes
- Validação de dados (CRM, COREN, CPF)
- Busca por nome, especialidade, setor

### Gestão de Consultas
- Criação de consultas (enfermeiros e médicos)
- Edição de consultas (apenas médicos)
- Visualização com controle de acesso por role
- Histórico completo de consultas
- Busca por período e filtros

### Comunicação Assíncrona
- Eventos de criação e atualização de consultas
- Processamento assíncrono com RabbitMQ
- Logs de auditoria automáticos

## 🏗️ Arquitetura

```
src/main/java/com/fiap/atividade3/
├── config/                 # Configurações (Security, GraphQL, Async)
├── graphql/               # Resolvers e tipos GraphQL
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

### Pré-requisitos
- Java 21+
- Maven 3.6+
- (Opcional) RabbitMQ para messaging assíncrono

### Passos

1. **Clone o repositório**
   ```bash
   git clone <repository-url>
   cd atividade3
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
   - Aplicação: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/api/swagger-ui/index.html`
   - H2 Console: `http://localhost:8080/api/h2-console`
   - GraphQL Playground: `http://localhost:8080/api/graphql`

## APIs Disponíveis

### REST Endpoints

#### Autenticação
- `POST /api/auth/login` - Login do usuário
- `GET /api/auth/me` - Informações do usuário atual
- `POST /api/auth/validate` - Validar token JWT

#### Consultas
- `GET /api/consultas/{id}` - Buscar consulta por ID
- `GET /api/consultas/minhas` - Consultas do usuário atual
- `GET /api/consultas/paciente/{id}` - Consultas por paciente
- `GET /api/consultas/medico/{id}` - Consultas por médico
- `GET /api/consultas/todas` - Todas as consultas (staff médico)
- `GET /api/consultas/recentes` - Consultas recentes (30 dias)
- `POST /api/consultas` - Criar nova consulta
- `PUT /api/consultas/{id}` - Atualizar consulta
- `DELETE /api/consultas/{id}` - Deletar consulta

### GraphQL

Acesse o GraphQL Playground em `http://localhost:8080/api/graphql` para:
- Explorar o schema completo
- Executar queries e mutations
- Testar subscriptions (se habilitadas)

#### Exemplo de Query
```graphql
query {
  currentUser {
    id
    nome
    email
    role
  }
}
```

#### Exemplo de Mutation
```graphql
mutation {
  login(input: {
    email: "joao.silva@hospital.com"
    senha: "senha123"
  }) {
    token
    usuario {
      id
      nome
      role
    }
  }
}
```

### 2. Buscar Consultas (Médico/Enfermeiro)
```graphql
query {
  todasConsultas {
    id
    dataConsulta
    motivo
    diagnostico
    paciente {
      nome
    }
    medico {
      nome
      especialidade
    }
  }
}
```

### 3. Consultas do Paciente
```graphql
query {
  minhasConsultas {
    id
    dataConsulta
    motivo
    diagnostico
    medico {
      nome
      especialidade
    }
  }
}
```

### 4. Criar Consulta
```graphql
mutation {
  criarConsulta(input: {
    dataConsulta: "2024-01-15T10:00:00"
    motivo: "Consulta de rotina"
    pacienteId: 1
    medicoId: 1
    enfermeiroId: 1
  }) {
    id
    dataConsulta
    motivo
    paciente {
      nome
    }
  }
}
```

### 5. Registrar Novo Paciente
```graphql
mutation {
  registrarPaciente(input: {
    nome: "João da Silva"
    email: "joao@email.com"
    senha: "senha123"
    cpf: "12345678901"
    dataNascimento: "1990-01-01"
    telefone: "(11) 99999-9999"
    endereco: "Rua das Flores, 123"
  }) {
    id
    nome
    email
    cpf
  }
}
```

## 🔒 Segurança

### Controle de Acesso por Role

| Operação | Médico | Enfermeiro | Paciente |
|----------|--------|------------|----------|
| Visualizar todas consultas | ✅ | ✅ | ❌ |
| Visualizar próprias consultas | ✅ | ✅ | ✅ |
| Criar consulta | ✅ | ✅ | ❌ |
| Editar consulta | ✅ | ❌ | ❌ |
| Deletar consulta | ✅ | ❌ | ❌ |
| Buscar usuários | ✅ | ✅ | ❌ |

### Headers de Autenticação
```
Authorization: Bearer <JWT_TOKEN>
```

## 📡 Mensageria Assíncrona

O sistema utiliza RabbitMQ para comunicação assíncrona:

- **Exchange**: `consulta.exchange`
- **Filas**: 
  - `consulta.created.queue` - Consultas criadas
  - `consulta.updated.queue` - Consultas atualizadas

## 🧪 Testes

Para executar os testes:
```bash
mvn test
```

## 📝 Configurações

### application.yml
- Configuração do banco H2
- Configuração JWT (secret, expiração)
- Configuração RabbitMQ
- Configuração de logs

### Variáveis de Ambiente
- `JWT_SECRET`: Chave secreta para JWT
- `JWT_EXPIRATION`: Tempo de expiração do token (ms)
- `RABBITMQ_HOST`: Host do RabbitMQ
- `RABBITMQ_PORT`: Porta do RabbitMQ

## 🚀 Deploy

Para deploy em produção:

1. Configure um banco de dados PostgreSQL/MySQL
2. Configure RabbitMQ em cluster
3. Ajuste as configurações de segurança
4. Configure HTTPS
5. Implemente monitoramento e logs

## 📚 Documentação Adicional

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [GraphQL Java Documentation](https://www.graphql-java.com/)
- [Spring Security Documentation](https://spring.io/projects/spring-security)

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
