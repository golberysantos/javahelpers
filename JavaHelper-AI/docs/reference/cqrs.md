# CQRS (Command Query Responsibility Segregation)

## Checklist

- [x] Definir CQRS e motivação
- [x] Explicar diferenças entre Commands e Queries
- [x] Descrever componentes (Write Model, Read Model, Command Handlers, Query Handlers)
- [x] Cobrir integração com Event Sourcing e Event-Driven Architecture
- [x] Fornecer exemplos conceituais e em Java/Spring
- [x] Enumerar vantagens, desvantagens e trade-offs
- [x] Listar padrões complementares (Projections, Read Models, Sagas, Outbox)
- [x] Incluir boas práticas, desafios comuns e mitigações
- [x] Adicionar referências e leituras recomendadas

---

## O que é CQRS?

CQRS (Command Query Responsibility Segregation) é um padrão arquitetural que separa as responsabilidades de escrita (commands) das responsabilidades de leitura (queries). A ideia central é que as operações que modificam estado (commands) e as operações que leem estado (queries) têm requisitos, modelos e otimizações diferentes, então manter caminhos e modelos distintos melhora clareza, performance e escalabilidade.

CQRS não exige eventos nem Event Sourcing, mas costuma ser usado em conjunto com Event Sourcing e arquiteturas orientadas a eventos.

## Conceitos Fundamentais

- Command: Representa uma intenção de mudar o sistema (ex.: PlaceOrder, CancelOrder). Commands são imperativos — "faça isso" — e podem ser validados ou rejeitados.
- Query: Solicitação de informação, sem efeito colateral (ex.: GetOrderById, ListOpenOrders).
- Write Model: Modelo otimizado para executar comandos e aplicar regras de domínio. Normalmente encapsula lógica e validações complexas.
- Read Model (Projection): Modelo otimizado para consultas. Pode ser denormalizado, indexado e armazenado em tecnologia diferente (SQL, NoSQL, ElasticSearch) para atender requisitos de leitura.
- Command Handler: Serviço responsável por receber um command, validar regras de negócio e executar a mudança (por exemplo, gerando eventos ou atualizando o banco).
- Query Handler: Serviço responsável por responder queries usando read models.

## Arquitetura Básica

Commands -> Command Handlers / Write Model -> (Event Store / Database)

Events -> Projections -> Read Models

Queries -> Query Handlers -> Read Models -> Resposta

Na prática:
- O caminho de escrita pode ser síncrono ou assíncrono. Em arquiteturas event-driven, o command gera eventos que são persistidos, e projeções são atualizadas assincronamente.
- O caminho de leitura normalmente acessa read models otimizados para consulta, que podem ser denormalizados e indexados para alta performance.

## Benefícios do CQRS

- Separação de preocupações clara entre leitura e escrita
- Possibilidade de otimizar cada caminho com tecnologias diferentes
- Melhora de escalabilidade (escalonamento independente de leitores e escritores)
- Facilita suporte a modelos de leitura complexos (denormalização) sem penalizar escrita
- Pode simplificar lógica de domínio ao deixar o write model focado nas invariantes

## Desvantagens e Trade-offs

- Maior complexidade arquitetural e operacional
- Consistência eventual entre write e read models (em implementações assíncronas)
- Mais componentes para testar, monitorar e operar
- Latência entre a execução de um command e a visibilidade do resultado na leitura (quando assíncrono)

## Integração com Event Sourcing

CQRS é frequentemente combinado com Event Sourcing:
- Commands causam mudanças expressas como eventos (Event Sourcing).
- Esses eventos são persistidos no Event Store (fonte de verdade).
- Projeções consomem eventos para construir read models.

Vantagens da combinação:
- Histórico completo das mudanças
- Fácil replay para reconstruir ou reindexar read models
- Natural alinhamento entre mudança (event) e projeção

Desvantagens:
- A complexidade do sistema aumenta (event store, replays, versionamento de eventos)

## Padrões Complementares

- Projection / Read Models: Consumidores de eventos que atualizam modelos de leitura otimizados.
- Outbox Pattern: Garante atomicidade entre gravação local de eventos e publicação para broker — escreva eventos (outbox) na mesma transação do banco e publique-os depois.
- Sagas / Process Managers: Coordenam workflows distribuídos e transações de longa duração através de eventos e comandos compensatórios.
- Query-side denormalization: Manter estruturas de dados específicas para cada caso de uso de leitura.
- CQRS with Materialized Views: Read models como views materializadas que são atualizadas a partir de eventos.

## Consistência e Visibilidade dos Dados

- Em versões assíncronas, a leitura é eventualmente consistente com a escrita.
- Se o aplicativo requer leitura imediata após uma escrita (read-your-writes), existem estratégias:
  - Ler do Write Model diretamente após comando (consumo de custo adicional e acoplamento)
  - Bloquear até que a projeção seja atualizada (não recomendado em geral)
  - Usar notificações/async callbacks para informar o cliente quando o read model estiver pronto

## Exemplo Conceitual (Java/Spring)

// Nota: exemplo simplificado, foco em estrutura.

```java
// Command
public class PlaceOrderCommand {
    public final String orderId;
    public final String customerId;
    public final List<OrderItem> items;
    // constructor/getters
}

// Command Handler
@Service
public class OrderCommandHandler {
    private final OrderRepository repo;
    private final EventPublisher publisher;

    public void handle(PlaceOrderCommand cmd) {
        OrderAggregate order = repo.loadOrCreate(cmd.getOrderId());
        order.place(cmd.getItems()); // valida regras de domínio
        List<Event> events = order.getUncommittedEvents();
        repo.saveEvents(cmd.getOrderId(), events);
        events.forEach(publisher::publish); // atualiza projections async
    }
}

// Query Handler (usando Read Model)
@Service
public class OrderQueryService {
    private final OrderReadRepository readRepo; // ex.: Elastic, Mongo

    public OrderDto getById(String orderId) {
        return readRepo.findById(orderId);
    }
}
```

Nesse padrão, após o command ser processado, o resultado só ficará disponível no read model depois que as projections consumirem os eventos publicados.

## Implementação com Kafka e Projeções

- Command -> Persist Events in Event Store -> Publish to Kafka topic
- Kafka consumers -> Atualizam read models (MongoDB, ElasticSearch)
- Queries -> Leem de read models otimizadas

Isso permite alta escalabilidade e resiliência, mas exige monitoramento das latências entre evento e projeção.

## Boas Práticas

- Documente contratos entre produtores de eventos e consumidores (schemas/registries)
- Mantenha read models pequenas e especializadas por caso de uso
- Faça handlers idempotentes e com bom tratamento de duplicatas
- Use Outbox para garantir publicação confiável a partir de uma transação local
- Monitore: latência de projeção, backlog de eventos, falhas em handlers
- Automatize replays de projeções e migrações de read models
- Considere a complexidade operacional antes de adotar CQRS em sistemas pequenos

## Testes e Validação

- Teste unitário do Write Model (aggregates) e de Command Handlers
- Teste de integração para garantir que eventos são persistidos e publicados
- Teste de contrato entre publishers e consumers (consumer-driven contract testing)
- Testes de performance e carga para validar escalabilidade dos read models

## Casos de Uso Adequados

- Domínios com regras de negócio complexas e invariantes fortes
- Sistemas que precisam escalar leitura e escrita de forma independente
- Cenários que se beneficiam de read models denormalizados (dashboards, relatórios)
- Aplicações que precisam de auditabilidade e capacidade de replay (quando combinado com Event Sourcing)

## Quando NÃO usar CQRS

- Aplicações simples CRUD sem requisitos de escala ou modelagem de leitura complexa
- Equipes sem experiência operacional com sistemas distribuídos e pipelines de eventos

## Observabilidade e Operação

- Implemente métricas para latência entre evento e projeção, taxa de erro em handlers, backlog de processamento
- Tracing distribuído (OpenTelemetry) para seguir o fluxo do command ao efetivo update da read model
- Logs estruturados e DLQs para eventos não processáveis

## Referências

- MS Patterns & Practices: CQRS guidance
- Greg Young: apresentações e artigos sobre CQRS e Event Sourcing
- Martin Fowler: artigos relacionados a CQRS e Event Sourcing
- Axon Framework documentation (Java)
- Kafka Streams / KSQL docs

---

## Diagrama

Inclua o diagrama visual abaixo para facilitar o entendimento do fluxo (commands -> events -> projections -> read models). Um SVG já está disponível em `docs/reference/diagrams/cqrs-diagram.svg`.

![CQRS Diagram](../diagrams/cqrs-diagram.svg)

## Exemplo runnable (Java + Spring Boot)

Um exemplo mínimo foi criado em `docs/examples/cqrs-demo`. Ele demonstra CQRS com um Event Store em memória e uma projeção simples.

Quickstart:

```powershell
cd docs/examples/cqrs-demo
mvn spring-boot:run
```

Endpoints:
- POST /orders  -> body: { "orderId": "o1", "customerId": "c1", "amount": 123.45 }
- GET /orders/{orderId} -> retorna o read model (JSON)

Este exemplo é didático — para produção use um Event Store persistente e mecanismos confiáveis de publicação (outbox, brokers duráveis).

---

Arquivo criado: `docs/reference/cqrs.md`
