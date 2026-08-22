# Event Sourcing

## Checklist

- [x] Definir o que é Event Sourcing
- [x] Explicar diferenças entre armazenar estado vs. armazenar eventos
- [x] Descrever componentes e conceitos chave (Event Store, Aggregate, Projections, Snapshots)
- [x] Cobrir práticas e padrões (append-only, versionamento, idempotência, concorrência otimista)
- [x] Enumerar vantagens, desvantagens e trade-offs
- [x] Mostrar exemplos de implementação (conceitual e em Java)
- [x] Sugerir tecnologias e ferramentas comuns
- [x] Fornecer best practices e problemas comuns com soluções
- [x] Incluir referências e links para leitura adicional

---

## O que é Event Sourcing?

Event Sourcing é um padrão arquitetural em que o estado de uma aplicação ou entidade (aggregate) é derivado a partir de uma sequência de eventos imutáveis que representam cada mudança relevante do domínio. Em vez de persistir o estado atual (por exemplo, uma linha em uma tabela), o sistema persiste cada evento que transformou o estado ao longo do tempo.

Em um modelo tradicional (CRUD), você armazena o estado atual. Em Event Sourcing, você armazena a história das mudanças (events) e reconstroi o estado quando necessário aplicando esses eventos em ordem.

## Por que usar Event Sourcing?

- Permite reconstruir o estado em qualquer ponto do tempo (auditoria completa).  
- Facilita o debug e a investigação de incidentes, porque você tem o histórico de decisões.  
- Suporta replay de eventos para recalcular projeções ou migrar modelos de leitura.  
- É natural quando combinado com CQRS (Command Query Responsibility Segregation) — comandos geram eventos e projeções são usadas para leitura.  

## Conceitos e Componentes

1. Event
- Um evento é uma mensagem imutável que descreve uma ocorrência de negócio (ex.: OrderPlaced, PaymentCaptured, ItemShipped).
- Deve conter um ID único, tipo, timestamp, versão (opcional), origem (aggregate id) e payload (dados relevantes).

2. Event Store
- Repositório append-only que persiste eventos em ordem. Fornece durabilidade e leitura por aggregateId e por intervalo de sequência.
- Suporta leitura por stream (por aggregate) e, em implementações avançadas, índices globais para consultas por tipo de evento.
- Ex.: EventStoreDB, Kafka (como log), bancos relacionais implementando append-only.

3. Aggregate
- Unidade de consistência do domínio (por exemplo, Order). Os aggregates aplicam eventos para progredir seu estado e expõem operações que geram novos eventos.
- Normalmente existe uma regra: apenas um aggregate é o responsável por gerar eventos para seu próprio stream.

4. Projections (Read Models)
- Estruturas de dados otimizadas para consulta, construídas a partir do stream de eventos. Podem ser atualizadas em tempo real ou por replay.
- São tipicamente armazenadas em bancos de dados otimizados para leitura (SQL, NoSQL, Elastic, etc.).

5. Snapshots
- Para evitar reconstituir um aggregate desde o primeiro evento, pode-se armazenar snapshots periódicos do estado. Na recuperação, aplica-se o snapshot e, em seguida, apenas os eventos posteriores.

6. Command
- Uma intenção de mudança (faça algo). Um command é validado pelo aggregate, que, se autorizado, gera um ou mais eventos.

7. Projeção/Event Handlers
- Consumidores de eventos que atualizam read models, acionam integrações externas ou produzem efeitos colaterais (notifications, emails, chamadas externas).

## Fluxo básico

1. Cliente envia um Command (ex.: PlaceOrder).  
2. O Command chega ao Aggregate responsável; o aggregate valida regras de negócio e, se válido, produz evento(s) (ex.: OrderPlaced).  
3. Os eventos são persistidos no Event Store (append-only).  
4. Event Handlers/Projections consomem os eventos e atualizam read models ou acionam efeitos externos.  

## Diferença entre Event Sourcing e Audit Log

- Audit Log registra mudanças (quem fez o quê) mas normalmente não é a fonte de verdade para o estado operacional.  
- Event Sourcing usa eventos como a fonte de verdade; o estado atual é derivado da sequência de eventos. Um audit log pode ser uma visão derivada dos eventos.

## Modelagem: Agregados e Invariantes

- Identifique agregados que possuem invariantes fortes (ex.: uma ordem com somatório de itens). Cada aggregate possui seu próprio stream de eventos.
- Invariantes que cruzam múltiplos aggregates podem precisar de sagas/process managers para coordenar mudanças e compensações.

## Concorrência e Consistência

- Uso comum de concorrência otimista: cada evento contém um número de versão (sequence/version). Ao persistir novas mudanças, o código verifica que a versão atual corresponde à esperada. Se não, ocorrerá conflito e é necessário re-tentar ou rejeitar.
- Em sistemas distribuídos, a consistência é geralmente eventual para projeções e integrações externas.

## Snapshotting

- Snapshot salva estado do aggregate em um determinado ponto de sequência. Na carga, carrega-se o snapshot e aplica-se apenas eventos posteriores.
- Políticas típicas: snapshot a cada N eventos, ou quando o custo de reconstrução excede um limiar.

## Idempotência e Delivery Semântica

- Consumidores de eventos devem ser idempotentes, pois brokers e pipelines podem entregar eventos múltiplas vezes (at-least-once delivery).
- Alternativas: tentar exactly-once (difícil) ou garantir idempotência pela deduplicação via eventId e armazenamento de estado já processado.

## Esquemas e Evolução de Eventos (Versionamento)

- Eventos são contratos: produtores e consumidores dependem do formato. É necessário planejar evolução do schema.
- Estratégias:
  - Versionar eventos (tipo v1, v2) e suportar múltiplas versões nos handlers.
  - Tornar campos opcionais e usar defaults quando novos campos aparecem.
  - Usar formatos com contratos (Avro, Protobuf) e um registro de schemas (schema registry) como parte da pipeline.

## Armazenamento e Ferramentas

- EventStoreDB: projeto dedicado a event sourcing com APIs ricas.  
- Apache Kafka: usado frequentemente como log distribuído e retenção de eventos; combina bem com CQRS e stream processing.  
- Bancos relacionais ou NoSQL: podem ser usados em padrões append-only (tabelas que nunca atualizam, apenas inserem eventos).  
- Frameworks: Axon Framework (Java), Akka Persistence (Scala/Java), Eventuate, Lagom, Marten (DotNet), EventFlow.

## Exemplos de Implementação (Conceitual em Java)

// Nota: o código abaixo é conceitual e omite detalhes de infraestrutura.

```java
// Evento simples
public class OrderPlaced {
    private final String orderId;
    private final String customerId;
    private final BigDecimal amount;
    private final long occurredAt;
    // construtores, getters
}

// Interface do Event Store
public interface EventStore {
    void append(String streamId, long expectedVersion, List<Event> events) throws ConcurrencyException;
    List<Event> load(String streamId, long fromVersion);
}

// Aggregate (reduced example)
public class OrderAggregate {
    private String orderId;
    private int version = 0; // versão corresponde ao último evento aplicado
    private BigDecimal total = BigDecimal.ZERO;

    public void apply(OrderPlaced e) {
        this.orderId = e.getOrderId();
        this.total = e.getAmount();
        this.version++;
    }

    public List<Event> placeOrder(String orderId, String customerId, BigDecimal amount) {
        // Validações de domínio
        OrderPlaced ev = new OrderPlaced(orderId, customerId, amount, System.currentTimeMillis());
        apply(ev);
        return List.of(ev);
    }
}

// Fluxo de persistência
OrderAggregate agg = repository.load(orderId); // reconstrói aplicando eventos
List<Event> newEvents = agg.placeOrder(...);
try {
    eventStore.append(orderId, agg.getVersion(), newEvents);
} catch (ConcurrencyException ex) {
    // estratégia de retry ou informar conflito ao usuário
}
```

## Projeções e Replays

- Projeções transformam eventos em read models. Se a projeção estiver corrompida ou o modelo de leitura precisar mudar, você pode replayar todo o stream para reconstruí-la a partir dos eventos armazenados.
- Para grandes volumes, usa-se snapshots e/ou processamento incremental (start from last checkpoint).

## Sagas / Process Managers

- Para transações distribuídas ou workflows longos, use sagas (process managers) que reagem a eventos, emitem comandos para outros aggregates e mantêm estado do processo.
- Sagas também lidam com compensações quando alguma etapa falha.

## Vantagens

- Auditoria completa e rastreabilidade total.  
- Permite reprocessar eventos para novas necessidades analíticas.  
- Facilita integração com stream processing, analytics e pipelines de ETL.  
- Compatível naturalmente com CQRS e arquiteturas orientadas a eventos.

## Desvantagens e Trade-offs

- Complexidade conceitual adicional — mais difícil de entender, desenvolver e operar.  
- Evolução de schema exige disciplina (versionamento, compatibilidade).  
- Reconstituir aggregates pode ser custoso sem snapshots.  
- Garantir atomicidade entre escrever eventos e publicar para handlers externos requer atenção (outbox pattern).  
- Debugging de fluxo assíncrono pode ser desafiador.

## Padrões Comuns para Operar Event Sourcing

1. Outbox Pattern
- Para garantir atomicidade entre persistir eventos localmente e publicar para um broker, escreva eventos e uma fila de saída (outbox) na mesma transação com a escrita do estado; um processo separa lê a outbox e publica no broker.

2. Transactional Append
- Persistir events com uma verificação otimista de versão para evitar condições de corrida.

3. Snapshotting
- Salvar snapshots de aggregates para acelerar reconstrução.

4. Schema Registry
- Usar um registro de schemas para versionamento e compatibilidade entre produtores e consumidores.

## Problemas Comuns e Mitigações

- Problema: Eventos duplicados -> Use idempotência via eventId, deduplicação ou armazenar marcas de processamento.  
- Problema: Incompatibilidade de eventos após deploy -> Versione eventos e compatibilize handlers.  
- Problema: Falha ao publicar para sistemas externos -> Use outbox + retry/backoff e DLQ (dead-letter queue).  
- Problema: Crescimento do log de eventos -> Estratégias de retenção, arquivamento ou compactação (dependendo da necessidade de replay histórico).

## Boas Práticas

- Mantenha eventos pequenos, com foco em fatos do domínio.  
- Defina contratos claros (schema) e documente-os.  
- Versione eventos de forma explícita.  
- Faça handlers idempotentes e monitore duplicatas.  
- Use snapshots quando os streams crescerem muito.  
- Monitore latência de processamento e integridade das projeções.  
- Automatize replays e testes de regressão das projeções.

## Quando NÃO usar Event Sourcing

- Aplicações simples com baixa necessidade de auditoria ou histórico detalhado.  
- Sistemas onde a complexidade adicional de eventos e replays supera os benefícios.  
- Situações onde o time não tem experiência ou suporte operacional para operar infra de eventos.

## Referências e Leituras Recomendadas

- Event Sourcing: Martin Fowler — https://martinfowler.com/eaaDev/EventSourcing.html
- Greg Young — Event Sourcing resources and talks
- EventStoreDB documentation — https://www.eventstore.com
- Apache Kafka documentation — https://kafka.apache.org
- Patrones: Outbox Pattern — Jimmy Bogard and Martin Fowler

---

Arquivo criado: `docs/reference/event-sourcing.md`
