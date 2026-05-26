# 📋 Kanban To-do — Projeto Desacoplado

Aplicação de gerenciamento de tarefas no estilo **Kanban**, com arquitetura desacoplada: backend em **Spring Boot** e frontend em **Angular**, comunicando-se via **API REST**.

---

## 🏗️ Arquitetura

```
To-do-desacoplado/
├── kanban-backend/      # API REST — Spring Boot + JPA + MySQL
└── todo-angular/        # SPA — Angular 17 + Angular CDK
```

O backend expõe uma API REST na porta **8088** e o frontend Angular consome essa API via `HttpClient`. Os dois projetos são completamente independentes e podem ser executados separadamente.

---

## ✨ Funcionalidades

- Criar tarefas com título
- Mover tarefas entre três colunas: **Pendente → Em Execução → Concluída**
- Arrastar e soltar cards entre colunas (drag & drop)
- Editar o título de uma tarefa
- Excluir tarefas
- Filtrar tarefas por status via query param
- Tema claro/escuro com preferência salva no `localStorage`
- Feedback visual de carregamento e erros

---

## 🛠️ Tecnologias

### Backend
| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.2.5 |
| Spring Data JPA | — |
| Spring Validation | — |
| MySQL | 8+ |
| Maven | — |

### Frontend
| Tecnologia | Versão |
|---|---|
| Angular | 17 |
| Angular CDK (Drag & Drop) | 17 |
| TypeScript | ~5.2 |
| RxJS | ~7.8 |

---

## 🚀 Como executar

### Pré-requisitos

- Java 21+
- Maven
- MySQL 8+
- Node.js 18+ e Angular CLI (`npm install -g @angular/cli`)

---

### 1. Backend

**Configure o banco de dados** em `kanban-backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/kanban_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=sua_senha
server.port=8088
```

> O banco `kanban_db` é criado automaticamente se não existir. O Hibernate também cria/atualiza as tabelas via `ddl-auto=update`.

**Execute o backend:**

```bash
cd kanban-backend
mvn spring-boot:run
```

A API ficará disponível em: `http://localhost:8088`

---

### 2. Frontend

**Configure a URL da API** em `todo-angular/src/app/services/tarefa.service.ts`:

```typescript
private readonly API = 'http://localhost:8088/api/tarefas';
```

**Instale as dependências e execute:**

```bash
cd todo-angular
npm install
ng serve
```

O frontend ficará disponível em: `http://localhost:4200`

---

## 📡 Endpoints da API

Base URL: `http://localhost:8088/api/tarefas`

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/tarefas` | Lista todas as tarefas |
| `GET` | `/api/tarefas?status=PENDENTE` | Filtra tarefas por status |
| `GET` | `/api/tarefas/{id}` | Busca uma tarefa pelo ID |
| `POST` | `/api/tarefas` | Cria uma nova tarefa |
| `PUT` | `/api/tarefas/{id}` | Edita o título de uma tarefa |
| `PATCH` | `/api/tarefas/{id}/status` | Atualiza o status de uma tarefa |
| `DELETE` | `/api/tarefas/{id}` | Remove uma tarefa |

### Exemplos de payload

**POST /api/tarefas**
```json
{ "titulo": "Minha nova tarefa" }
```

**PATCH /api/tarefas/{id}/status**
```json
{ "status": "EM_EXECUCAO" }
```

**Valores válidos para `status`:** `PENDENTE` · `EM_EXECUCAO` · `CONCLUIDA`

---

## 🗂️ Estrutura do Backend

```
com.kanban/
├── KanbanApplication.java          # Ponto de entrada
├── controller/
│   ├── TarefaController.java       # Endpoints REST
│   └── CorsConfig.java             # Configuração de CORS
├── service/
│   └── TarefaService.java          # Regras de negócio
├── repository/
│   └── TarefaRepository.java       # Acesso ao banco (Spring Data JPA)
├── model/
│   ├── Tarefa.java                 # Entidade JPA
│   └── StatusTarefa.java           # Enum: PENDENTE, EM_EXECUCAO, CONCLUIDA
├── dto/
│   └── TarefaDTO.java              # DTOs de entrada e saída
└── exception/
    ├── TarefaNaoEncontradaException.java
    └── GlobalExceptionHandler.java
```

## 🗂️ Estrutura do Frontend

```
src/app/
├── app.component.ts                # Componente raiz
├── app.config.ts                   # Configuração da aplicação
├── models/
│   └── tarefa.model.ts             # Interface Tarefa e tipo StatusTarefa
├── services/
│   └── tarefa.service.ts           # Serviço HTTP (consome a API REST)
└── components/
    └── todo/
        ├── todo.component.ts       # Lógica do quadro Kanban
        ├── todo.component.html     # Template com as três colunas
        └── todo.component.css      # Estilos do componente
```

---

## 📄 Licença

Distribuído sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.