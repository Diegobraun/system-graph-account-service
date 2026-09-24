# account-service

Serviço de clientes e contas. Usa **spring-kafka** direto (`KafkaTemplate` e `@KafkaListener`), expõe REST e
**GraphQL** (Spring for GraphQL) e chama o loan-service com **OpenFeign**.

## Contratos

| Tipo | Contrato | Detalhe |
|---|---|---|
| REST exposto | `POST /customers` | cria cliente com `name`, `document`, `monthlyIncome` |
| REST exposto | `GET /customers/{id}` | chamado pelo loan-service |
| REST exposto | `POST /accounts` | abre conta para um cliente e publica `account-opened` |
| REST exposto | `GET /accounts/{id}` | chamado pelo loan-service |
| REST exposto | `GET /accounts/{id}/summary` | saldo e empréstimos da conta, busca os empréstimos no loan-service |
| GraphQL exposto | `query customer(id)` / `query account(id)` | schema em [`schema.graphqls`](src/main/resources/graphql/schema.graphqls); `Customer.accounts` resolvido por `@SchemaMapping` |
| REST chamado | `GET /loans?accountId=` no loan-service | `LoanClient`, `@FeignClient(name = "loan-service")` |
| Kafka publica | `account-opened` | `AccountOpenedEvent(accountId, customerId, openedAt)` via `KafkaTemplate` |
| Kafka consome | `loan-disbursed` | `LoanDisbursedEvent`, credita o valor na conta (idempotente por `loanId`) |

GraphQL fica em `http://localhost:8081/graphql`.

O tópico publicado vem de uma constante (`Topics.ACCOUNT_OPENED`) e o consumido de uma propriedade
(`${app.topics.loan-disbursed}`). Os dois casos são de propósito, para exercitar o extrator.

## Configuração relevante

- `spring.application.name: account-service`: identifica o serviço no grafo.
- `spring.kafka.consumer.group-id: account-service`: mesmo nome, para cruzar com o runtime do Kafka.
- `spring.json.add.type.headers: false`: o evento não carrega o nome da classe Java, então o consumidor usa a
  própria classe. É isso que permite a divergência de payload com o loan-service passar despercebida em runtime.

## Rodando sozinho

```bash
mvn spring-boot:run
```

Porta 8081. Precisa do Kafka em `localhost:9092` e do loan-service em `localhost:8082` para o `/summary`.
O Kafka sobe com o `docker-compose.yml` da [plataforma](https://github.com/Diegobraun/system-graph-poc).

## Integração com IA

`.mcp.json` aponta para o MCP server do grafo e o `CLAUDE.md` orienta o assistente a consultar
`impact_of_change` antes de mexer em contratos.

## Parte da POC system-graph

Este repositório é um dos serviços da POC [system-graph](https://github.com/Diegobraun/system-graph-poc), que dá a assistentes de IA uma visão
dos contratos entre serviços que vivem em repositórios diferentes.

| Repositório | Papel |
|---|---|
| [system-graph-poc](https://github.com/Diegobraun/system-graph-poc) | Plataforma: extrator, MCP server, templates de CI, docker-compose e docs |
| [system-graph-account-service](https://github.com/Diegobraun/system-graph-account-service) | Clientes e contas |
| [system-graph-loan-service](https://github.com/Diegobraun/system-graph-loan-service) | Empréstimos |

### O que este repositório tem para o grafo

- **`.github/workflows/system-graph.yml`**: a cada push na `main`, compila, baixa o `graph-extractor.jar` da
  release da plataforma, extrai o `service-graph.json` e publica como artefato do workflow. Se os secrets
  `NEO4J_URI`, `NEO4J_USER` e `NEO4J_PASSWORD` existirem, também grava no Neo4j.
- **`.gitlab-ci.yml`**: o mesmo job no formato GitLab, incluindo o template da plataforma. É o que um serviço da
  empresa teria.
- **`.mcp.json`** e **`CLAUDE.md`**: conectam o assistente ao MCP server e dizem quando consultar o grafo.

O extrator só enxerga este repositório. O cruzamento com os outros serviços acontece no grafo central.
