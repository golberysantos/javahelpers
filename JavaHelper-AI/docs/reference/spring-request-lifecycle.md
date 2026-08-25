# Spring Request Lifecycle


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


Nota: As annotations não são a arquitetura do Controller. Elas são metadados que permitem ao Spring MVC conectar o contrato externo ao comportamento do Controller.
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

O `HandlerMapping` usa dois critérios:
1. **URL path** → definido por `@RequestMapping` e `@PostMapping` (e similares)
2. **Verbo HTTP** → POST, GET, PUT, DELETE, etc.

Fluxo simplificado:

```text
POST /chat
    │
    ▼
HandlerMapping
  (URL + Verbo HTTP)
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

`@RestController` marca a classe como um **Controller REST** e é a combinação de:

```text
@Controller + @ResponseBody
```

**O que isso significa:**
- A classe é um Controller (recebe requisições HTTP)
- Todo retorno dos métodos é **automaticamente serializado** (geralmente para JSON) e escrito direto no corpo da resposta HTTP
- Não há resolução de view (HTML/JSP) — a resposta é sempre o objeto convertido

**Quem realiza a conversão:**
O `HttpMessageConverter` (por padrão, `MappingJackson2HttpMessageConverter` quando Jackson está disponível) faz a conversão:

```text
Objeto Java
    │
    ▼
@RestController (@ResponseBody)
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
Corpo da Resposta HTTP
```

Importante:

> `@RestController` não é Jackson e não realiza diretamente a conversão JSON.

O fluxo de saída envolve o mecanismo do Spring MVC e um `HttpMessageConverter`. Quando JSON é o formato escolhido e Jackson está disponível, um converter baseado em Jackson realiza a conversão.

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

`@RequestBody` instrui o Spring a pegar o **corpo da requisição HTTP** (o payload, geralmente JSON) e desserializá-lo automaticamente no objeto Java do parâmetro do método.

**O que ele faz:**
Indica que o parâmetro deve ser **obtido a partir do corpo HTTP**, não de query strings ou path variables.

**Quem executa a conversão:**
O mesmo `HttpMessageConverter` citado acima, só que no sentido inverso:

```text
JSON (Corpo HTTP)
    │
    ▼
HttpMessageConverter
    │
    ▼
Jackson
    │
    ▼
PerguntaDTO (Objeto Java)
```

Exemplo:

```http
POST /chat
Content-Type: application/json

{
    "pergunta": "Explique herança"
}
```

é convertido em:

```java
PerguntaDTO pergunta
```

**Importante:**
`@RequestBody` não é o conversor JSON. Sua responsabilidade está **relacionada à origem do argumento** — ele diz ao Spring que o valor vem do body, e o `HttpMessageConverter` faz a conversão propriamente dita.

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

---

## 📚 Próximo Passo: Mecanismos de Resolução de Argumentos

Para um entendimento profundo de **como o Spring resolve os argumentos de métodos** (o tópico **8. Argument Resolution** desta documentação), consulte o documento complementar:

### 👉 [**spring-argument-resolvers.md**](./spring-argument-resolvers.md)

Nesse documento você encontrará:

- **HandlerMethodArgumentResolver**: O mecanismo central que resolve cada parâmetro
- **Query String vs Body JSON**: Por que são completamente diferentes
- **Cenários práticos**: Exemplos detalhados de cada anotação
- **Tabela comparativa**: Quando usar `@RequestBody`, `@RequestParam`, `@ModelAttribute`, etc.
- **Boas práticas**: Recomendações para evitar erros comuns
- **Dúvidas frequentes**: Respostas às perguntas mais cirúrgicas

### Resumo Rápido

A pergunta clássica é: **"Por que o Spring consegue preencher um objeto a partir de parâmetros de query string, mas não faz o mesmo com body JSON sem `@RequestBody`?"**

**Resposta curta:**
- `@RequestBody` ativa o **RequestResponseBodyMethodProcessor**, que lê o `InputStream` e usa `HttpMessageConverter` + Jackson
- Sem anotação, o Spring ativa o **ServletModelAttributeMethodProcessor** (fallback), que lê apenas `request.getParameterMap()` (query/form data)
- O `WebDataBinder` não foi projetado para desserializar JSON do body

**Regra de ouro:**
- JSON no body → use `@RequestBody`
- Query string / Form data → use `@RequestParam` ou `@ModelAttribute` (ou fallback sem anotação)
- URL path → use `@PathVariable`

---

## 9.1 Anotações Principais: Resumo Prático

Aqui está um resumo de **quem faz o quê** com as anotações mais comuns:

### **@RestController**
Marca a classe como um controller REST. É a combinação de `@Controller` + `@ResponseBody` — o Spring já assume que **todo retorno dos métodos vai ser serializado** (geralmente para JSON) e escrito direto no corpo da resposta HTTP, sem precisar resolver uma view (HTML/JSP).

**Responsável pelo mecanismo:** o `HttpMessageConverter` (por padrão, `MappingJackson2HttpMessageConverter`) faz a conversão objeto → JSON.

### **@RequestMapping("/chat")**
Define o **path base** da classe (ou de um método específico). Todo endpoint dentro do controller herda esse prefixo `/chat`. 

**Quem processa:** o `DispatcherServlet` consulta o `HandlerMapping` para descobrir qual controller/método atende à URL requisitada.

### **@PostMapping**
Atalho de `@RequestMapping(method = RequestMethod.POST)`. Diz que aquele método só responde a requisições HTTP **POST** naquele path. 

**Faz parte:** do mesmo mecanismo de `HandlerMapping` — usa o verbo HTTP como critério extra de casamento (matching) da rota.

### **@RequestBody**
Instrui o Spring a pegar o **corpo da requisição HTTP** (o payload, geralmente JSON) e desserializá-lo automaticamente no objeto Java do parâmetro do método. 

**Quem executa:** o mesmo `HttpMessageConverter`, só que no sentido inverso (JSON → objeto).

### **Resumo do fluxo (quem faz o quê):**

1. **Requisição chega** → `DispatcherServlet` recebe
2. **`HandlerMapping`** casa URL (`@RequestMapping`) + verbo (`@PostMapping`)
3. **`HttpMessageConverter`** desserializa o body (`@RequestBody`) em objeto Java
4. **Método executa** e retorna um objeto
5. **`@RestController`** garante que esse retorno seja serializado (via `HttpMessageConverter`) e escrito na resposta, sem passar por view resolver

### **Diagrama visual:**

```text
POST /chat
  ├─ DispatcherServlet recebe
  ├─ HandlerMapping identifica: @RequestMapping("/chat") + @PostMapping
  ├─ HttpMessageConverter desserializa body (@RequestBody) → PerguntaDTO
  ├─ ChatController.perguntar(perguntaDTO) executa
  ├─ Retorna RespostaDTO
  ├─ @RestController garante serialização
  └─ HttpMessageConverter serializa RespostaDTO → JSON
```

---

## 9.2 Nota Importante: Testes (QA)

Em testes de unidade com **`@WebMvcTest`** + **`MockMvc`**, você está **simulando exatamente essa cadeia** que foi descrita acima.

**Pontos de falha comuns:**

1. **Matching de rota:** A URL, verbo HTTP, path variables não casam com `@RequestMapping` + `@PostMapping`
2. **(De)serialização do payload:** Campos com nomes diferentes no JSON vs no DTO; tipos incompatíveis; campos faltando

**Exemplo de teste:**
```java
@WebMvcTest(ChatController.class)
class ChatControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void devePerguntarComSucesso() throws Exception {
        String perguntaJson = """
            {
                "pergunta": "Explique herança"
            }
            """;
        
        mockMvc.perform(post("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(perguntaJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
```

**O que está sendo testado:**
- ✅ Rota (`/chat`) + verbo (`POST`) → casam com `@PostMapping`?
- ✅ Desserialização: JSON → `PerguntaDTO` → funciona?
- ✅ Serialização: `RespostaDTO` → JSON → funciona?
- ✅ O status HTTP é o esperado?
- ✅ O `Content-Type` da resposta é `application/json`?

Essa é uma forma eficaz de validar que toda a cadeia de Spring MVC está funcionando corretamente.
