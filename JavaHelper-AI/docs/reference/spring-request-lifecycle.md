O ciclo de vida de uma requisição no **Spring MVC** (e Spring Boot) descreve o caminho exato que um protocolo HTTP percorre desde o momento em que chega ao servidor até a devolução da resposta ao cliente.

O coração desse processo é o padrão **Front Controller**, implementado no Spring pela classe central `DispatcherServlet`.

Cliente
   │
   │ POST /chat
   ▼
Tomcat
   │
   ▼
Servlet Filters
   │
   ▼
DispatcherServlet
       │
       ▼
HandlerMapping
       │
       ▼
HandlerExecutionChain

---

## O Ciclo de Vida Passo a Passo

1. **Filtros Servlet (Servlet Filters):** Antes de entrar no contexto do Spring MVC.
A requisição HTTP chega ao container Servlet (como Tomcat). Ela passa pela cadeia de **Servlet Filters** (`FilterChain`), como `CorsFilter` ou `Spring Security Filter Chain`. Aqui ocorrem validações globais, como autenticação inicial e verificação de cabeçalhos.


2. **Chegada ao DispatcherServlet:** Entrada no Spring Web MVC.
Se os filtros permitirem a passagem, a requisição é entregue ao `DispatcherServlet` (o Front Controller). Ele orquestra todos os componentes subsequentes.

O DispatcherServlet é o componente central e o coração do Spring MVC. Ele implementa o padrão de projeto Front Controller, funcionando como o único ponto de entrada para todas as requisições HTTP enviadas à aplicação.

Em vez de cada rota ou endpoint ter seu próprio Servlet individual, o DispatcherServlet intercepta todas as chamadas e gerencia o fluxo de execução delegando as tarefas aos componentes especializados do Spring.


3. **Mapeamento de Handlers (HandlerMapping):**
O `DispatcherServlet` consulta o `HandlerMapping` para descobrir qual Controller e qual método específico deve processar a requisição (com base na URL e no verbo HTTP, como `@GetMapping`). O `HandlerMapping` retorna um objeto chamado `HandlerExecutionChain`.

O HandlerMapping é responsável por encontrar qual Controller e qual método devem processar a requisição.

O HandlerExecutionChain é um objeto que guarda o controlador (handler) escolhido para uma requisição e uma lista de interceptores (HandlerInterceptor) que rodam antes e depois dele. O DispatcherServlet usa essa cadeia para processar requisições web de forma organizada.

4. **Execução de Interceptadores (Pre-Handle):** HandlerInterceptor.
Antes de chamar o Controller, o Spring executa o método `preHandle()` dos **HandlerInterceptors** registrados. Se algum interceptador retornar `false`, a execução é abortada imediatamente.


5. **Adaptador de Handler (HandlerAdapter):**
O `DispatcherServlet` envia o handler para o `HandlerAdapter`. O adaptador é responsável por invocar o método do seu `@RestController` ou `@Controller`, resolvendo parâmetros de entrada (como `@RequestBody`, `@PathVariable`, `@RequestParam`).


6. **Execução do Controller:** Sua regra de negócio.
O método do Controller executa a lógica necessária, geralmente chamando serviços (`@Service`) e repositórios (`@Repository`).


7. **Processamento do Retorno (HttpMessageConverter ou ViewResolver):**
* **Em APIs REST (`@RestController` / `@ResponseBody`):** O `HttpMessageConverter` (como Jackson) serializa o objeto retornado diretamente para JSON/XML na resposta HTTP.
* **Em Aplicações MVC tradicionais:** O Controller retorna o nome de uma View (ModelAndView), e o `ViewResolver` traduz esse nome para um template HTML (como Thymeleaf).


8. **Pós-processamento dos Interceptadores (Post-Handle e After-Completion):**
O método `postHandle()` é chamado após a execução do Controller, e o `afterCompletion()` é executado após a renderização final/envio da resposta, ideal para limpeza de recursos e logs de auditoria.


9. **Resposta HTTP entregue ao Cliente:**
A resposta formatada retorna pela cadeia de Servlet Filters até ser enviada de volta ao navegador ou cliente HTTP.


---

## Componentes Principais

| Componente | Papel no Ciclo de Vida |
| --- | --- |
| **DispatcherServlet** | Orquestrador central que recebe e direciona as requisições. |
| **HandlerMapping** | Associa a URL recebida ao Controller correto. |
| **HandlerAdapter** | Executa o método do Controller e injeta os argumentos necessários. |
| **HttpMessageConverter** | Converte objetos Java em JSON/XML (e vice-versa usando Jackson). |
| **ViewResolver** | Resolve nomes de visões em arquivos HTML/Templates. |

@PostMapping participa do mapeamento, @RequestBody indica que o corpo HTTP deve ser associado ao parâmetro, @RestController incorpora o comportamento de @ResponseBody, enquanto o HttpMessageConverter participa da conversão e o Jackson realiza a serialização/desserialização.

---

## Filtros (Filters) vs Interceptadores (Interceptors)

Uma dúvida clássica no ciclo de vida é a diferença entre **Filters** e **Interceptors**:

* **Servlet Filters:** Operam no nível do Container Servlet (Tomcat/Jetty), **antes** que o Spring MVC assuma o controle. Ideais para segurança, compactação (gzip) e CORS.
* **HandlerInterceptors:** Operam **dentro** do Spring MVC, entre o `DispatcherServlet` e o Controller. Têm acesso ao contexto do Spring e aos metadados do handler (anotações, métodos executados).




## Conceitos envolvidos

- IoC
- DI
- Polimorfismo
- Spring Bean
- Application Context
- Jackson
- Spring MVC

## Perguntas para revisão

...


## Dúvidas em aberto

- Como o Spring Boot registra o Jackson?
- Como os HttpMessageConverters são configurados?
- Como o @RequestBody determina qual objeto deve ser criado?
- Como o Spring encontra o método associado a POST /chat?
- Qual é exatamente o papel do DispatcherServlet?


## Nota
Entrada: @RequestBody participa da conversão do corpo HTTP para um objeto Java.
Saída: @RestController implica comportamento de @ResponseBody, permitindo que o retorno do método seja escrito no corpo da resposta HTTP.


# Spring Request Lifecycle

## Objetivo

Documentar o fluxo básico de uma requisição HTTP dentro do Spring MVC no JavaHelper AI, conectando HTTP, Servlet Container, Servlet Filters, DispatcherServlet, HandlerMapping, Controllers, argument resolution, HttpMessageConverter, Jackson e DTOs.

Este documento apresenta um modelo didático simplificado do fluxo. O Spring MVC possui outras etapas e mecanismos internos que não estão representados aqui.

---

## 1. Visão geral

Uma requisição ao JavaHelper AI percorre, de forma simplificada, o seguinte fluxo:

```text
HTTP Request
     │
     ▼
Servlet Container (Tomcat)
     │
     ▼
Servlet Filter Chain
     │
     ├── CORS
     ├── Security
     └── outros filtros
     │
     ▼
DispatcherServlet
     │
     ▼
HandlerMapping
     │
     ▼
ChatController
     │
     ▼
Argument Resolution
     │
     ▼
@RequestBody
     │
     ▼
HttpMessageConverter
     │
     ▼
Jackson
     │
     ▼
PerguntaDTO
     │
     ▼
ChatService
     │
     ▼
Application
```

---

## 2. Servlet Container

Antes de entrar no contexto do Spring MVC, a requisição HTTP chega ao **Servlet Container**, como o Tomcat.

O container fornece a infraestrutura necessária para receber e processar requisições HTTP e encaminhá-las para os componentes Servlet.

Fluxo simplificado:

```text
HTTP Request
     │
     ▼
Servlet Container
     │
     ▼
Servlet Filter Chain
```

---

## 3. Servlet Filters

Os **Servlet Filters** participam do processamento da requisição antes de ela chegar ao `DispatcherServlet`.

Eles são executados por meio de uma cadeia (`FilterChain`) e podem realizar comportamentos transversais, como:

- CORS;
- verificações relacionadas à segurança;
- validações globais;
- processamento de cabeçalhos;
- outros comportamentos de infraestrutura.

Um exemplo importante é a **Spring Security Filter Chain**, que utiliza o mecanismo de filtros do Servlet para aplicar os comportamentos de segurança configurados.

Fluxo:

```text
HTTP Request
     │
     ▼
Servlet Container
     │
     ▼
Filter Chain
     │
     ├── CORS
     ├── Security
     └── outros filtros
     │
     ▼
DispatcherServlet
```

> Observação: o comportamento concreto dos filtros depende da configuração da aplicação.

---

## 4. DispatcherServlet

O `DispatcherServlet` atua como o ponto central de entrada do Spring MVC para as requisições HTTP.

De forma simplificada:

```text
HTTP Request
     │
     ▼
DispatcherServlet
```

Ele participa do encaminhamento da requisição para o componente responsável por tratá-la.

---

## 5. HandlerMapping

O `HandlerMapping` participa da identificação do handler apropriado para uma determinada requisição.

Considerando:

```java
@RequestMapping("/chat")
@PostMapping
```

uma requisição:

```http
POST /chat
```

pode ser associada ao método:

```text
ChatController.perguntar()
```

Fluxo simplificado:

```text
POST /chat
    │
    ▼
HandlerMapping
    │
    ▼
ChatController.perguntar()
```

---

## 6. ChatController

O `ChatController` pertence à camada **Presentation**.

Sua responsabilidade é receber a entrada externa, adaptá-la para a Application e devolver a resposta ao cliente.

Ele não deve conhecer a implementação do caso de uso.

Exemplo:

```java
@RestController
@RequestMapping("/chat")
public class ChatController {

    @PostMapping
    public RespostaDTO perguntar(
            @RequestBody PerguntaDTO pergunta) {

        return ...;
    }
}
```

O Controller conhece o contrato da Application, mas não precisa conhecer como a resposta é produzida.

---

## 7. @RestController

`@RestController` é uma composição de:

```text
@Controller
+
@ResponseBody
```

Isso significa que a classe é tratada como um Controller e que os retornos dos métodos são tratados como corpo da resposta HTTP.

Importante:

> `@RestController` não é Jackson e não realiza diretamente a conversão JSON.

O fluxo de saída envolve o mecanismo do Spring MVC e um `HttpMessageConverter`. Quando JSON é o formato escolhido e Jackson está disponível, um converter baseado em Jackson pode realizar a conversão.

Fluxo:

```text
Controller
    │
    ▼
@ResponseBody
    │
    ▼
HttpMessageConverter
    │
    ▼
Jackson
    │
    ▼
JSON
```

---

## 8. Argument Resolution

O Spring MVC possui diferentes mecanismos para resolver os argumentos necessários para invocar um método de Controller.

Exemplo:

```java
public RespostaDTO perguntar(
        @RequestBody PerguntaDTO pergunta)
```

O Spring precisa produzir o valor de:

```text
PerguntaDTO pergunta
```

para então conseguir executar:

```text
perguntar(pergunta)
```

A presença de `@RequestBody` indica que esse argumento deve ser obtido a partir do corpo HTTP.

---

## 9. @RequestBody

`@RequestBody` informa ao Spring MVC que o parâmetro deve ser obtido a partir do corpo da requisição HTTP.

Ele não é o conversor JSON.

Sua responsabilidade está relacionada à origem do argumento:

```text
HTTP Body
    │
    ▼
@RequestBody
    │
    ▼
mecanismo de resolução do argumento
```

A conversão do conteúdo para o tipo Java envolve o `HttpMessageConverter`.

---

## 10. HttpMessageConverter

O `HttpMessageConverter` participa da conversão entre a representação HTTP e objetos Java.

### Entrada

```text
HTTP Body
    │
    ▼
HttpMessageConverter
    │
    ▼
Objeto Java
```

### Saída

```text
Objeto Java
    │
    ▼
HttpMessageConverter
    │
    ▼
HTTP Response Body
```

Quando o conteúdo é JSON, um converter apropriado pode utilizar Jackson.

---

## 11. Jackson

Jackson é uma biblioteca utilizada para processar JSON e objetos Java.

### Desserialização

```text
JSON
  │
  ▼
Jackson
  │
  ▼
PerguntaDTO
```

Exemplo:

```json
{
    "pergunta": "Explique herança"
}
```

é convertido em uma representação Java:

```java
PerguntaDTO
```

### Serialização

O caminho inverso:

```text
RespostaDTO
    │
    ▼
Jackson
    │
    ▼
JSON
```

Portanto:

```text
Serialização    = Objeto Java → JSON
Desserialização = JSON → Objeto Java
```

---

## 12. Content-Type e Accept

Considere:

```http
POST /chat
Content-Type: application/json
Accept: application/json

{
    "pergunta": "Explique herança"
}
```

### Content-Type

Indica o formato do conteúdo enviado na requisição.

```http
Content-Type: application/json
```

Pergunta respondida:

> O que estou enviando?

### Accept

Indica o formato que o cliente deseja receber.

```http
Accept: application/json
```

Pergunta respondida:

> O que quero receber?

Esses headers participam da escolha dos mecanismos de conversão adequados.

---

## 13. DTO e Application

O `PerguntaDTO` pertence à **Presentation** porque representa o contrato de entrada da interface externa.

O Controller pode extrair o dado necessário:

```java
pergunta.getPergunta()
```

e entregá-lo à Application:

```java
chatService.perguntar(pergunta.getPergunta());
```

A Application não deve depender de modelos específicos da camada de entrada/Presentation.

Essa separação permite que mudanças no mecanismo externo não precisem atravessar desnecessariamente as camadas internas.

---

## 14. Contratos

É importante distinguir os contratos existentes.

### Contrato HTTP

Define como o mundo externo conversa com a API:

```http
POST /chat
Content-Type: application/json

{
    "pergunta": "Explique herança"
}
```

### Contrato da Application

Define a capacidade oferecida pela aplicação:

```java
String perguntar(String pergunta);
```

O Controller faz a adaptação entre esses dois mundos:

```text
Mundo externo
     │
     │ Contrato HTTP
     ▼
ChatController
     │
     │ Contrato da Application
     ▼
ChatService
```

O `ChatController` consome o contrato de `ChatService`; ele não o implementa.

---

## 15. Fluxo completo de entrada

```text
HTTP Request
     │
     ▼
Servlet Container
     │
     ▼
Servlet Filter Chain
     │
     ▼
DispatcherServlet
     │
     ▼
HandlerMapping
     │
     ▼
ChatController
     │
     ▼
Argument Resolution
     │
     ▼
@RequestBody
     │
     ▼
HttpMessageConverter
     │
     ▼
Jackson
     │
     ▼
PerguntaDTO
     │
     ▼
ChatService
     │
     ▼
Application
     │
     ▼
Domain
```

---

## 16. Fluxo completo de saída

Após a execução do caso de uso:

```text
Domain / Application
        │
        ▼
ChatController
        │
        ▼
RespostaDTO
        │
        ▼
HttpMessageConverter
        │
        ▼
Jackson
        │
        ▼
JSON
        │
        ▼
HTTP Response
```

A resposta também passa pela infraestrutura do Servlet Container e pela cadeia de filtros conforme a configuração da aplicação.

---

## 17. Responsabilidade de cada componente

| Elemento | Responsabilidade |
|---|---|
| Servlet Container | Receber e processar a infraestrutura HTTP/Servlet |
| Servlet Filter | Executar comportamentos transversais antes/depois do processamento Servlet |
| `@RestController` | Identificar o Controller REST e incorporar o comportamento de `@ResponseBody` |
| `@RequestMapping` | Definir o mapeamento base |
| `@PostMapping` | Mapear o método para requisições POST compatíveis |
| `@RequestBody` | Indicar que o parâmetro deve ser obtido do body HTTP |
| `HandlerMapping` | Participar da identificação do handler |
| Argument Resolution | Resolver os argumentos necessários para invocar o método |
| `HttpMessageConverter` | Participar da conversão HTTP ↔ objetos Java |
| Jackson | Processar JSON ↔ objetos Java |
| DTO | Representar o contrato de entrada/saída da Presentation |
| Controller | Adaptar a entrada/saída e conversar com a Application |
| `ChatService` | Representar o caso de uso/capacidade da Application |

---

## 18. Por que essa separação importa?

Uma mudança no contrato externo não deve necessariamente alterar a regra de negócio.

Por exemplo:

```text
REST/JSON
    │
    ▼
PerguntaDTO
    │
    ▼
Application
```

pode futuramente ser substituído por:

```text
GraphQL
    │
    ▼
GraphQL Input
    │
    ▼
Application
```

sem que a Application precise conhecer detalhes específicos da tecnologia de entrada.

### Princípio arquitetural

> **A Application não deve depender de modelos específicos da camada de entrada/Presentation.**

O objetivo não é impedir mudanças.

O objetivo é **confinar o impacto das mudanças**.

---

## 19. Modelo mental final

```text
                         HTTP
                          │
                          ▼
               ┌────────────────────┐
               │ Servlet Container   │
               │      (Tomcat)       │
               └─────────┬──────────┘
                         │
                         ▼
               ┌────────────────────┐
               │   Filter Chain      │
               │ CORS / Security /   │
               │ outros filtros      │
               └─────────┬──────────┘
                         │
                         ▼
               ┌────────────────────┐
               │  DispatcherServlet  │
               └─────────┬──────────┘
                         │
                         ▼
               ┌────────────────────┐
               │   HandlerMapping    │
               └─────────┬──────────┘
                         │
                         ▼
               ┌────────────────────┐
               │  ChatController     │
               └─────────┬──────────┘
                         │
                  Argument Resolution
                         │
                    @RequestBody
                         │
                         ▼
               ┌────────────────────┐
               │ HttpMessageConverter│
               └─────────┬──────────┘
                         │
                         ▼
                      Jackson
                         │
                         ▼
                   PerguntaDTO
                         │
                         ▼
                    ChatService
                         │
                         ▼
                    Application
                         │
                         ▼
                      Domain
```

### Fluxo de retorno

```text
Domain / Application
        │
        ▼
   ChatController
        │
        ▼
   RespostaDTO
        │
        ▼
HttpMessageConverter
        │
        ▼
     Jackson
        │
        ▼
       JSON
        │
        ▼
 HTTP Response
```

---

## 20. Observação final

Este documento descreve um **modelo didático simplificado** do ciclo de requisição do JavaHelper AI.

O Spring MVC possui outros componentes, etapas e mecanismos internos que não estão representados aqui. O objetivo deste documento é registrar o modelo mental necessário para compreender a interação entre HTTP, Servlet Container, filtros, Spring MVC, Controllers, DTOs, conversores, Jackson e as camadas internas da aplicação.
