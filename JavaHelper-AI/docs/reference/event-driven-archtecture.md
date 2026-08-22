# Event-Driven Architecture

## Introdução

**Event-Driven Architecture** (Arquitetura Orientada a Eventos) é um padrão arquitetural que promove a produção, detecção, consumo e reação a eventos. É um estilo de arquitetura distribuída que facilita a criação de sistemas altamente desacoplados, escaláveis e responsivos.

## O que é um Evento?

Um evento é uma mudança de estado significativa em um sistema. Exemplos:
- Um usuário fez login
- Uma ordem foi criada
- Um pagamento foi processado
- Um arquivo foi deletado
- Uma temperatura atingiu um limite

Um evento típico contém:
```json
{
  "id": "evt-12345",
  "type": "OrderCreated",
  "timestamp": "2026-08-22T10:30:00Z",
  "source": "OrderService",
  "data": {
    "orderId": "ord-789",
    "customerId": "cust-456",
    "amount": 299.99
  }
}
```

## Componentes Principais

### 1. **Event Producer (Produtor de Eventos)**
- Gera e publica eventos quando algo importante acontece
- Não se importa quem vai consumir o evento
- Exemplo: Serviço de Pedidos que publica "OrderCreated"

### 2. **Event Broker (Intermediário de Eventos)**
- Middleware que recebe eventos dos produtores e os distribui aos consumidores
- Pode ser uma fila de mensagens (RabbitMQ, Kafka) ou um event hub (Azure Event Hubs)
- Responsável pelo roteamento e entrega confiável

### 3. **Event Consumer (Consumidor de Eventos)**
- Processa eventos quando recebidos
- Reage aos eventos fazendo alguma ação
- Exemplo: Serviço de Notificações que envia um email ao receber "OrderCreated"

### 4. **Event Store (Armazenamento de Eventos)**
- Armazena todos os eventos que ocorreram no sistema
- Permite auditoria, replay de eventos e reconstrução de estado
- Base para implementar Event Sourcing

## Padrões de Comunicação

### 1. **Pub/Sub (Publish/Subscribe)**
```
OrderService (Publicador)
    |
    v
Event Broker
    |
    +----> EmailService (Subscriber)
    +----> InventoryService (Subscriber)
    +----> AnalyticsService (Subscriber)
```
- Um evento é publicado para múltiplos subscribers
- Desacoplamento entre produtor e consumidores
- Os subscribers se registram para eventos de seu interesse

### 2. **Event Streaming**
```
OrderService -> Kafka Topic -> Múltiplos Consumers (simultaneamente)
                            -> Event Store
                            -> Analytics
```
- Eventos fluem continuamente através de um tópico
- Múltiplos consumidores podem processar o mesmo evento
- Kafka é o exemplo clássico

## Vantagens

✅ **Desacoplamento**: Serviços não precisam conhecer uns aos outros diretamente  
✅ **Escalabilidade**: Fácil adicionar novos consumidores ou produtores  
✅ **Reatividade**: Sistema responde em tempo real aos eventos  
✅ **Auditoria**: Histórico completo de mudanças no sistema  
✅ **Resiliência**: Se um consumidor falha, o evento ainda existe no broker  
✅ **Flexibilidade**: Novos consumidores podem ser adicionados sem alterar produtores  

## Desvantagens

❌ **Complexidade**: Mais difícil de entender e debugar  
❌ **Consistência Eventual**: Dados podem estar temporariamente inconsistentes  
❌ **Ordenação de Eventos**: Pode ser difícil garantir ordem em sistemas distribuídos  
❌ **Overhead de Infraestrutura**: Requer brokers, armazenamento de eventos, etc.  
❌ **Debugging Difícil**: Fluxo de controle não é linear  

## Arquiteturas Relacionadas

### Event Sourcing
Ao invés de armazenar o estado atual, armazenam todos os eventos que levaram a esse estado.

```
Estado Tradicional:
User { name: "João", balance: 1000 }

Event Sourcing:
- UserCreated { userId: 1, name: "João" }
- BalanceUpdated { userId: 1, amount: 1000 }
- BalanceDeducted { userId: 1, amount: 100 }
- Resultado: balance = 900
```

**Benefícios:**
- Auditoria completa
- Reconstrução de estado em qualquer ponto no tempo
- Replay de eventos para análise

### CQRS (Command Query Responsibility Segregation)
Separa operações de escrita (Commands) de leitura (Queries).

```
Commands (Escrita) -> Event Stream -> Read Models (Leitura)
```

## Exemplos de Implementação

### Usando Java e Spring

```java
// Evento
public class OrderCreatedEvent {
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    // getters, setters
}

// Produtor
@Service
public class OrderService {
    @Autowired
    private ApplicationEventPublisher publisher;
    
    public void createOrder(Order order) {
        // Lógica de criação
        publisher.publishEvent(new OrderCreatedEvent(order));
    }
}

// Consumidor
@Service
public class EmailService {
    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Enviar email para o cliente
        sendEmail(event.getCustomerId());
    }
}
```

### Usando Kafka

```java
// Produtor
@Service
public class OrderProducer {
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    public void produceOrderEvent(OrderEvent event) {
        kafkaTemplate.send("order-topic", event);
    }
}

// Consumidor
@Service
public class OrderConsumer {
    @KafkaListener(topics = "order-topic", groupId = "group_id")
    public void consumeOrderEvent(OrderEvent event) {
        // Processar o evento
    }
}
```

## Tecnologias Populares

| Tecnologia | Tipo | Características |
|-----------|------|-----------------|
| **Apache Kafka** | Streaming | Alta throughput, particionamento, retenção |
| **RabbitMQ** | Message Broker | AMQP, roteamento flexível |
| **AWS SNS/SQS** | Cloud | Gerenciado, escalável |
| **Azure Event Hubs** | Cloud | Tempo real, integrado com Azure |
| **Google Pub/Sub** | Cloud | Totalmente gerenciado |
| **Redis Streams** | In-Memory | Baixa latência, persistência |
| **Apache Pulsar** | Streaming | Multi-tenancy, durabilidade |

## Casos de Uso

1. **E-commerce**: Pedidos -> Notificações -> Inventário -> Pagamento
2. **Redes Sociais**: Postagem criada -> Notificar seguidores -> Atualizar timeline
3. **IoT**: Sensores enviam leituras -> Processamento -> Alertas
4. **Análise em Tempo Real**: Eventos -> Analytics -> Dashboard
5. **Microserviços**: Comunicação assíncrona entre serviços
6. **Notificações**: SMS, Email, Push notifications em tempo real

## Desafios Comuns

### 1. Ordenação de Eventos
```
Desafio: Evento A deve ser processado antes de B
Solução: Usar partições (Kafka) ou sequenciamento de eventos
```

### 2. Entrega Duplicada
```
Desafio: Mesmo evento processado duas vezes
Solução: Implementar idempotência no consumidor
```

### 3. Consistência Eventual
```
Desafio: Dados temporariamente inconsistentes
Solução: Design para eventual consistency, acrescentar compensações
```

### 4. Monitoramento e Observabilidade
```
Solução: Logging estruturado, tracing distribuído, métricas
```

## Best Practices

✔️ **Versione seus eventos** - Adicione versão para evolução segura  
✔️ **Use IDs únicos** - Cada evento deve ter um ID único para rastreamento  
✔️ **Adicione timestamps** - Essencial para ordenação e auditoria  
✔️ **Implemente retry logic** - Para falhas transitórias  
✔️ **Documente contratos de eventos** - Schema do evento deve ser claro  
✔️ **Teste idempotência** - Consumidores devem lidar com duplicatas  
✔️ **Monitore latência** - Rastreie tempo entre produção e consumo  
✔️ **Use circuit breakers** - Proteja contra falhas em cascata  

## Exemplo Prático: Sistema de Pedidos

```
1. Cliente cria pedido
   ↓
2. OrderService publica "OrderCreated"
   ↓
3. Múltiplos serviços reagem:
   - EmailService: Envia confirmação
   - InventoryService: Reserva itens
   - PaymentService: Processa pagamento
   - AnalyticsService: Registra métrica
   
4. Se PaymentService falhar:
   - InventoryService pode reverter a reserva (compensação)
   - Customer é notificado
   
5. Todo o histórico é armazenado no Event Store
```

## Conclusão

Event-Driven Architecture é um padrão poderoso para construir sistemas distribuídos, escaláveis e responsivos. É particularmente efetivo em:
- Microserviços
- Sistemas em tempo real
- Aplicações que requerem auditoria completa
- Cenários com alta concorrência

A escolha de usar deve ser baseada nos requisitos específicos do projeto, pois adiciona complexidade significativa ao sistema.

---

**Referências:**
- Apache Kafka Documentation
- AWS Event-Driven Architecture
- Microsoft: Event Sourcing Pattern
- Sam Newman: Building Microservices