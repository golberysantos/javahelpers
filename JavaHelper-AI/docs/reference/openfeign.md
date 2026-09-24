
# OpenFeign — Guia detalhado

## 1. O que é OpenFeign?

**OpenFeign** é um cliente HTTP declarativo utilizado para facilitar a comunicação entre aplicações, especialmente em arquiteturas de **microsserviços**.

Em vez de escrever manualmente código para construir uma requisição HTTP, configurar URL, headers, serializar JSON, executar a chamada e desserializar a resposta, você declara uma **interface Java** que representa o serviço remoto.

Imagine que temos:

```text
┌──────────────────┐          HTTP           ┌──────────────────┐
│   Pedido API     │ ──────────────────────> │   Pagamento API  │
│                  │                         │                  │
│ Spring Boot      │                         │ Spring Boot      │
└──────────────────┘                         └──────────────────┘
```

Sem Feign, você poderia utilizar `RestClient`, por exemplo:

```java
RestClient restClient = RestClient.builder()
        .baseUrl("http://pagamento-service")
        .build();

Pagamento pagamento = restClient.get()
        .uri("/pagamentos/{id}", id)
        .retrieve()
        .body(Pagamento.class);
```

Com OpenFeign, a ideia é declarar:

```java
@FeignClient(name = "pagamento-service")
public interface PagamentoClient {

    @GetMapping("/pagamentos/{id}")
    Pagamento buscar(@PathVariable Long id);
}
```

E então:

```java
Pagamento pagamento = pagamentoClient.buscar(10L);
```

A grande ideia é:

> **Você descreve a API remota através de uma interface Java e o framework cuida da implementação do cliente HTTP.**

---

# 2. Por que ele é chamado de "declarativo"?

Essa é uma palavra importante para entender Feign.

Existem duas abordagens.

### Imperativa

Você diz **como** executar a chamada:

```java
HttpRequest request = ...;
HttpResponse response = ...;
```

Você controla explicitamente:

1. criação da requisição;
2. URL;
3. headers;
4. método HTTP;
5. execução;
6. tratamento da resposta;
7. conversão do JSON.

### Declarativa

Você descreve **o que deseja chamar**:

```java
@FeignClient(name = "cliente-service")
public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    Cliente buscarPorId(@PathVariable Long id);
}
```

Você não implementa:

```java
public Cliente buscarPorId(Long id) {
    // HTTP...
}
```

O Feign gera essa implementação.

É semelhante à ideia de:

```java
interface Repository {
    Cliente findById(Long id);
}
```

Você trabalha contra uma **abstração**, enquanto o framework fornece a implementação.

---

# 3. OpenFeign não é o servidor

Essa distinção é fundamental.

OpenFeign é utilizado para **consumir APIs**.

Ele fica do lado do cliente:

```text
                 HTTP
┌──────────────┐ ───────────────> ┌──────────────┐
│              │                  │              │
│ OpenFeign    │                  │ REST API     │
│              │                  │              │
│ HTTP CLIENT  │                  │ SERVER       │
└──────────────┘                  └──────────────┘
```

Por exemplo:

```text
PedidoService
      │
      │ Feign
      ▼
PagamentoService
```

O `PagamentoService` pode ter:

```java
@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
}
```

Enquanto o `PedidoService` possui:

```java
@FeignClient(name = "pagamento-service")
public interface PagamentoClient {
}
```

Portanto:

**Controller → expõe API**

**Feign Client → consome API**

---

# 4. Dependência no Spring Boot

Em um projeto Spring Cloud, normalmente adicionamos o OpenFeign através do starter correspondente.

Por exemplo, em Maven:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

E habilitamos:

```java
@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

O:

```java
@EnableFeignClients
```

informa ao Spring para procurar interfaces declaradas como:

```java
@FeignClient
```

---

# 5. Criando nosso primeiro Feign Client

Imagine que exista esta API:

```http
GET http://localhost:8081/clientes/10
```

Retornando:

```json
{
    "id": 10,
    "nome": "Golbery"
}
```

Criamos:

```java
@FeignClient(
    name = "cliente-client",
    url = "${clientes.url}"
)
public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    Cliente buscarPorId(@PathVariable Long id);
}
```

No `application.yml`:

```yaml
clientes:
  url: http://localhost:8081
```

Agora podemos utilizar:

```java
@Service
public class PedidoService {

    private final ClienteClient clienteClient;

    public PedidoService(ClienteClient clienteClient) {
        this.clienteClient = clienteClient;
    }

    public Cliente consultarCliente(Long id) {
        return clienteClient.buscarPorId(id);
    }
}
```

Perceba algo interessante:

```java
clienteClient.buscarPorId(id);
```

parece uma chamada Java normal.

Mas por trás dela existe:

```text
Java
 ↓
Feign Proxy
 ↓
HTTP Request
 ↓
Servidor remoto
 ↓
HTTP Response
 ↓
Desserialização
 ↓
Objeto Java
```

---

# 6. O que acontece internamente?

Aqui começa a parte mais interessante para uma visão de engenharia.

Quando você declara:

```java
@FeignClient
public interface ClienteClient {
}
```

essa interface não possui uma implementação concreta escrita por você.

O Spring/Feign cria um **proxy**.

Conceitualmente:

```text
ClienteClient
      │
      ▼
┌────────────────────┐
│ Feign Proxy        │
│                    │
│ intercepta chamada │
└─────────┬──────────┘
          │
          ▼
   monta HTTP request
          │
          ▼
      HTTP Client
          │
          ▼
       Internet
```

Quando você executa:

```java
clienteClient.buscarPorId(10L);
```

o proxy interpreta as anotações:

```java
@GetMapping("/clientes/{id}")
@PathVariable
```

e consegue determinar algo equivalente a:

```http
GET /clientes/10
```

Depois o response:

```json
{
    "id": 10,
    "nome": "Golbery"
}
```

é convertido para:

```java
Cliente
```

através do mecanismo de conversão configurado no ecossistema Spring.

---

# 7. Feign e Jackson

Aqui existe uma integração importante.

Imagine:

```java
public record Cliente(
    Long id,
    String nome
) {}
```

A API responde:

```json
{
    "id": 10,
    "nome": "Golbery"
}
```

Feign precisa transformar:

```text
JSON
 ↓
Cliente
```

No Spring Cloud OpenFeign, essa integração normalmente passa pelos mecanismos de conversão HTTP do Spring.

Conceitualmente:

```text
HTTP Response
     │
     ▼
JSON
     │
     ▼
HttpMessageConverter
     │
     ▼
Jackson
     │
     ▼
Cliente
```

Isso conecta diretamente com aquilo que estudamos sobre:

```java
@RequestBody
```

e Jackson.

No servidor:

```text
JSON → Jackson → Java
```

No cliente Feign:

```text
Java → JSON → HTTP
```

ou:

```text
HTTP → JSON → Java
```

dependendo da direção da comunicação.

---

# 8. GET com PathVariable

Exemplo:

```java
@FeignClient(
    name = "cliente-client",
    url = "${clientes.url}"
)
public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    Cliente buscarPorId(@PathVariable("id") Long id);
}
```

Chamando:

```java
clienteClient.buscarPorId(10L);
```

equivale aproximadamente a:

```http
GET /clientes/10
```

---

# 9. Query Parameters

Suponha:

```http
GET /clientes?nome=Golbery
```

Feign:

```java
@GetMapping("/clientes")
List<Cliente> buscar(
    @RequestParam("nome") String nome
);
```

Uso:

```java
clienteClient.buscar("Golbery");
```

Resultado:

```http
GET /clientes?nome=Golbery
```

---

# 10. POST com RequestBody

Agora:

```http
POST /pagamentos
```

com:

```json
{
    "valor": 100.00,
    "clienteId": 10
}
```

Feign:

```java
@PostMapping("/pagamentos")
Pagamento criar(
    @RequestBody CriarPagamentoRequest request
);
```

Uso:

```java
CriarPagamentoRequest request =
        new CriarPagamentoRequest(10L, BigDecimal.valueOf(100));

Pagamento pagamento = pagamentoClient.criar(request);
```

O Feign transforma o objeto em JSON e envia:

```http
POST /pagamentos
Content-Type: application/json
```

```json
{
    "clienteId": 10,
    "valor": 100
}
```

---

# 11. Headers

Podemos precisar enviar:

```http
Authorization: Bearer eyJ...
```

Uma possibilidade é:

```java
@GetMapping("/clientes/{id}")
Cliente buscar(
    @PathVariable Long id,
    @RequestHeader("Authorization") String token
);
```

Chamando:

```java
clienteClient.buscar(10L, token);
```

Isso funciona, mas em sistemas reais existe uma abordagem mais interessante para determinados casos: **interceptors**.

---

# 12. Feign Interceptor

Imagine que todas as chamadas precisam receber:

```http
Authorization: Bearer TOKEN
```

Não seria interessante fazer:

```java
buscar(id, token);
listar(token);
criar(request, token);
atualizar(request, token);
```

em todos os métodos.

Podemos centralizar isso.

Conceitualmente:

```text
Feign Request
     │
     ▼
Interceptor
     │
     ├── adiciona Authorization
     ├── adiciona Correlation-ID
     ├── adiciona headers
     │
     ▼
HTTP Request
```

Exemplo:

```java
@Bean
public RequestInterceptor authInterceptor() {
    return template -> {
        template.header(
            "Authorization",
            "Bearer " + obterToken()
        );
    };
}
```

Isso é muito poderoso em arquiteturas distribuídas.

---

# 13. Service Discovery

Aqui entramos em uma questão arquitetural importante.

Você pode configurar:

```yaml
clientes:
  url: http://localhost:8081
```

Isso funciona muito bem em desenvolvimento.

Mas imagine produção:

```text
cliente-service
 ├── instance-01
 ├── instance-02
 ├── instance-03
 └── instance-04
```

Qual IP você utiliza?

É aí que entra o conceito de **Service Discovery**.

Em vez de:

```java
@FeignClient(
    name = "cliente-client",
    url = "http://10.20.30.40:8081"
)
```

podemos trabalhar conceitualmente com:

```java
@FeignClient(name = "cliente-service")
```

E um mecanismo de descoberta fornece a localização da instância.

Arquiteturalmente:

```text
              Service Discovery
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
       Cliente 1  Cliente 2  Cliente 3
          ▲
          │
          │
     Feign Client
          │
          │
     Pedido Service
```

Isso é especialmente relevante em ambientes de microsserviços.

---

# 14. Load Balancing

Agora imagine:

```text
cliente-service

Instance A
Instance B
Instance C
```

O cliente precisa escolher para onde enviar a requisição.

Um mecanismo de load balancing pode fazer:

```text
Request 1 → A
Request 2 → B
Request 3 → C
Request 4 → A
```

Então:

```text
Feign
  ↓
Load Balancer
  ↓
Service Instance
```

Essa combinação é muito comum em arquiteturas Spring Cloud.

---

# 15. Feign + Resilience

Aqui está uma das partes que eu considero **mais importantes para você como futuro/atual Tech Lead**.

Imagine:

```text
PedidoService
     │
     │ Feign
     ▼
PagamentoService
```

E o `PagamentoService` está indisponível.

O problema é que uma falha remota pode se propagar:

```text
PagamentoService DOWN
       ↓
PedidoService espera
       ↓
threads ocupadas
       ↓
latência aumenta
       ↓
mais requisições acumulam
       ↓
PedidoService começa a falhar
```

Isso pode virar um problema de **cascading failure**.

Por isso, comunicação HTTP entre microsserviços precisa considerar:

* timeout;
* retry;
* circuit breaker;
* fallback;
* observabilidade;
* idempotência.

---

# 16. Timeout

Nunca pense:

> "É uma chamada interna, então vai responder rápido."

Uma chamada remota é uma chamada remota.

Mesmo:

```text
Service A → Service B
```

envolve:

```text
DNS
 ↓
TCP
 ↓
TLS
 ↓
HTTP
 ↓
processamento
 ↓
banco
 ↓
resposta
```

Pode haver lentidão em qualquer ponto.

Por isso:

```text
timeout
```

é fundamental.

Um timeout evita que o consumidor fique esperando indefinidamente.

---

# 17. Retry

Imagine:

```text
PedidoService
     │
     ├── request
     ▼
PagamentoService
     │
     X timeout
```

Um retry pode tentar novamente:

```text
1ª tentativa → falha
2ª tentativa → sucesso
```

Mas existe uma armadilha enorme:

### Nem toda operação pode ser repetida com segurança.

Imagine:

```http
POST /pagamentos
```

A primeira requisição pode ter sido processada, mas a resposta se perdeu.

Feign pensa:

```text
"Não recebi resposta."
```

Retry:

```text
POST /pagamentos novamente
```

Agora podemos ter:

```text
Pagamento #123
Pagamento #124
```

Ou seja:

> **Retry sem considerar idempotência pode transformar uma falha de comunicação em duplicação de negócio.**

Esse é exatamente o tipo de detalhe que diferencia:

**"sei usar Feign"**

de

**"sei projetar comunicação entre microsserviços".**

---

# 18. Circuit Breaker

Outra estratégia é:

```text
Feign
  ↓
Circuit Breaker
  ↓
Serviço remoto
```

Se o serviço remoto começa a falhar repetidamente:

```text
SUCCESS
SUCCESS
ERROR
ERROR
ERROR
ERROR
```

o circuito pode abrir:

```text
       ┌─────────────┐
       │   OPEN      │
       │             │
       │ não chama   │
       │ serviço     │
       └─────────────┘
```

Em vez de continuar enviando requisições para um serviço que está indisponível, o sistema pode falhar rapidamente ou executar um comportamento alternativo.

Depois de determinado período, pode testar novamente.

---

# 19. Fallback

Imagine:

```java
clienteClient.buscar(id);
```

e o serviço remoto está indisponível.

Dependendo da arquitetura, você pode ter um comportamento alternativo.

Por exemplo:

```text
Consulta cliente
      │
      X
      │
      ▼
Cache
      │
      ▼
dados anteriormente armazenados
```

Ou:

```text
Serviço indisponível
       ↓
retorna estado controlado
```

Mas cuidado:

> **Fallback não deve esconder uma falha de negócio.**

Por exemplo, retornar:

```json
{
    "saldo": 0
}
```

porque o serviço bancário está indisponível pode ser extremamente perigoso.

---

# 20. OpenFeign e arquitetura limpa

Aqui temos uma conexão direta com o seu projeto **JavaHelper-AI**.

Imagine:

```text
Presentation
      │
      ▼
Application
      │
      ▼
Infrastructure
      │
      ▼
Feign
```

Eu evitaria espalhar chamadas Feign por todo o sistema.

Por exemplo:

```java
@Service
public class ChatService {

    private final GeminiFeignClient geminiClient;

}
```

pode funcionar.

Mas em uma arquitetura mais limpa podemos ter uma abstração:

```java
public interface AiGateway {

    Resposta gerar(String pergunta);
}
```

E na infraestrutura:

```java
@Component
public class GeminiAiGateway implements AiGateway {

    private final GeminiFeignClient client;

    // ...
}
```

Então:

```text
Application
     │
     │ depende de
     ▼
 AiGateway
     ▲
     │ implementação
     │
Infrastructure
     │
     ▼
OpenFeign
     │
     ▼
Gemini API
```

Isso reduz o acoplamento da camada de aplicação ao mecanismo HTTP.

---

# 21. Feign não significa necessariamente microsserviços

Outro ponto importante.

Você pode utilizar OpenFeign para consumir:

```text
Microsserviço
```

mas também:

```text
API de terceiros
```

por exemplo:

```text
Sua aplicação
     │
     ▼
OpenFeign
     │
     ▼
API externa
```

Como:

```text
Pagamento
CEP
Open Banking
IA
ERP
CRM
```

No seu caso, isso é especialmente relevante porque seus projetos trabalham com APIs de IA.

---

# 22. Feign + APIs de IA

Imagine seu projeto:

```text
JavaHelper
    │
    ▼
ChatService
    │
    ▼
GeminiClient
    │
    ▼
Google Gemini API
```

Você poderia ter:

```java
@FeignClient(
    name = "gemini-client",
    url = "${gemini.url}"
)
public interface GeminiClient {

    @PostMapping("/generateContent")
    GeminiResponse generate(
        @RequestBody GeminiRequest request
    );
}
```

Seu código fica muito mais expressivo:

```java
GeminiResponse response =
        geminiClient.generate(request);
```

em vez de controlar manualmente:

```text
HTTP client
headers
URL
JSON
response
status
serialization
```

---

# 23. Feign vs RestClient vs WebClient

Essa comparação é muito importante.

| Tecnologia     | Estilo              | Uso típico                                  |
| -------------- | ------------------- | ------------------------------------------- |
| `RestClient`   | Imperativo/síncrono | APIs HTTP                                   |
| `WebClient`    | Reativo             | Comunicação reativa                         |
| OpenFeign      | Declarativo         | Clientes HTTP, especialmente microsserviços |
| `RestTemplate` | Imperativo          | Legado                                      |

Uma forma simples de pensar:

```text
RestClient
"Eu vou construir a chamada HTTP."

Feign
"Esta interface representa a API HTTP."

WebClient
"Quero trabalhar com comunicação reativa."
```

---

# 24. OpenFeign não elimina HTTP

Isso parece óbvio, mas é uma confusão comum.

Quando você escreve:

```java
clienteClient.buscarPorId(10L);
```

pode parecer uma chamada local:

```java
clienteRepository.findById(10L);
```

Mas **não é**.

Repository:

```text
Application
   │
   ▼
Database
```

Feign:

```text
Application
   │
   ▼
Network
   │
   ▼
Outro processo
```

Portanto, Feign tem todas as características de uma chamada distribuída:

* latência;
* timeout;
* indisponibilidade;
* falha de rede;
* autenticação;
* observabilidade;
* versionamento;
* compatibilidade de contratos.

---

# 25. Um erro conceitual perigoso

Imagine:

```java
for (Pedido pedido : pedidos) {
    clienteClient.buscarPorId(pedido.clienteId());
}
```

Se tivermos:

```text
1.000 pedidos
```

podemos gerar:

```text
1.000 chamadas HTTP
```

Isso é o famoso problema de **N+1 remoto**.

```text
PedidoService
   │
   ├── HTTP → Cliente
   ├── HTTP → Cliente
   ├── HTTP → Cliente
   ├── HTTP → Cliente
   ├── ...
   └── HTTP → Cliente
```

Uma chamada remota é muito mais cara do que uma simples chamada de método.

Por isso, ao utilizar Feign, sempre pergunte:

> **Quantas chamadas remotas estou produzindo?**

Essa é uma ótima pergunta de Code Review.

---

# 26. Contrato entre serviços

Outro conceito importante.

Se temos:

```java
@FeignClient(name = "cliente-service")
public interface ClienteClient {

    @GetMapping("/clientes/{id}")
    Cliente buscar(@PathVariable Long id);
}
```

estamos assumindo um contrato:

```text
GET /clientes/{id}
```

com determinado formato de resposta.

Se o servidor mudar:

```json
{
    "customerId": 10,
    "fullName": "Golbery"
}
```

mas o consumidor espera:

```json
{
    "id": 10,
    "nome": "Golbery"
}
```

temos um problema de compatibilidade.

Então Feign não resolve sozinho:

> **Contract management**

Ele apenas facilita o consumo do contrato.

---

# 27. Observabilidade

Em microsserviços, eu também pensaria em:

```text
Feign
 ↓
Logs
 ↓
Metrics
 ↓
Tracing
```

Imagine:

```text
Request ID: abc-123
```

A chamada:

```text
API Gateway
    ↓
Pedido Service
    ↓
Feign
    ↓
Pagamento Service
    ↓
Database
```

deveria permitir rastrear a requisição.

Isso é fundamental para troubleshooting.

E aqui entra uma palavra que você está treinando para entrevistas:

**troubleshooting**

Pronúncia aproximada:

> **TRÂ-bol-shu-ting**

---

# 28. Como pensar como Tech Lead

Quando alguém diz:

> "Vamos usar OpenFeign."

Eu não responderia simplesmente:

> "Ok."

Eu faria perguntas:

### 1. Qual serviço estamos chamando?

```text
Interno?
Externo?
```

### 2. Qual é o SLA?

```text
100 ms?
1 s?
5 s?
```

### 3. Qual timeout?

```text
connect timeout
read timeout
```

### 4. Existe retry?

Se sim:

```text
Qual operação?
Quantas tentativas?
Backoff?
É idempotente?
```

### 5. O que acontece se o serviço estiver indisponível?

```text
Fallback?
Circuit breaker?
Erro imediato?
```

### 6. Como autenticar?

```text
OAuth2?
JWT?
API Key?
mTLS?
```

### 7. Como observar?

```text
Logs?
Metrics?
Tracing?
Correlation ID?
```

### 8. Existe risco de N+1?

```text
Quantas chamadas remotas por request?
```

### 9. Como o contrato é versionado?

```text
v1?
v2?
Backward compatibility?
```

### 10. A aplicação realmente precisa dessa chamada síncrona?

Talvez seja melhor:

```text
REST síncrono
```

ou:

```text
evento assíncrono
```

como:

```text
Kafka
SQS
SNS
Kinesis
```

Essa última pergunta é particularmente importante em arquiteturas distribuídas.

---

# 29. O modelo mental que quero que você guarde

Se você lembrar apenas de uma coisa, guarde este desenho:

```text
┌───────────────────────────────────────────┐
│              SUA APLICAÇÃO                │
│                                           │
│  Controller                               │
│      │                                    │
│      ▼                                    │
│  Application Service                      │
│      │                                    │
│      ▼                                    │
│  Gateway / Client                         │
│      │                                    │
│      ▼                                    │
│  ┌─────────────────────────────┐          │
│  │       OpenFeign             │          │
│  │                             │          │
│  │  Proxy                      │          │
│  │  Serialization              │          │
│  │  HTTP Client                │          │
│  │  Interceptors               │          │
│  └──────────────┬──────────────┘          │
│                 │                         │
└─────────────────┼─────────────────────────┘
                  │
                 HTTP
                  │
                  ▼
        ┌──────────────────────┐
        │ Serviço remoto       │
        │                      │
        │ Controller           │
        │ Service              │
        │ Repository           │
        └──────────────────────┘
```

E, em produção:

```text
              ┌── Timeout
              ├── Retry
              ├── Circuit Breaker
              ├── Load Balancing
              ├── Service Discovery
              ├── Authentication
              ├── Observability
              └── Idempotency
                     │
                     ▼
                  Feign
                     │
                     ▼
               HTTP Service
```

---

# 30. OpenFeign em uma frase de entrevista

Se perguntarem:

> **"What is OpenFeign?"**

Você poderia responder:

> **"OpenFeign is a declarative HTTP client that allows a Spring application to consume REST APIs through Java interfaces. Instead of manually building HTTP requests, we define the remote API contract using annotations, and Feign handles the HTTP communication and serialization."**

Uma versão ainda mais curta para entrevista:

> **"OpenFeign is a declarative HTTP client. It lets us define REST API clients as Java interfaces, reducing boilerplate and making service-to-service communication easier to maintain."**

---

## 🎯 O ponto mais importante para seu nível

Golbery, eu colocaria OpenFeign dentro deste mapa mental:

```text
                  DISTRIBUTED SYSTEMS
                         │
          ┌──────────────┼──────────────┐
          │              │              │
        REST           Events         gRPC
          │              │
          ▼              ▼
       Feign          Kafka/SQS
          │
          ▼
     HTTP Client
          │
   ┌──────┼──────────┐
   │      │          │
Timeout Retry   Circuit Breaker
   │      │          │
   └──────┼──────────┘
          │
          ▼
    Resilience
```

**Feign não é simplesmente uma biblioteca para "fazer GET e POST".**

