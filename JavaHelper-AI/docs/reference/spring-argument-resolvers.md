# Mecanismos de Resolução de Argumentos no Spring MVC

> Documento complementar ao **spring-request-lifecycle.md**

Este documento detalha como o Spring MVC resolve os argumentos de métodos de Controller, explicando as diferenças entre `@RequestBody`, `@ModelAttribute`, `@RequestParam` e outros mecanismos.

---

## 1. HandlerMethodArgumentResolver: O Mecanismo Central

### Conceito Fundamental

No Spring MVC, quando um método de Controller tem parâmetros, o framework precisa resolver **como** obter o valor de cada parâmetro a partir da requisição HTTP.

Essa responsabilidade é delegada a uma cadeia de resolvedores chamados **HandlerMethodArgumentResolver**.

```text
Requisição HTTP
        │
        ▼
HandlerMethodArgumentResolverComposite
        │
        ├─ RequestResponseBodyMethodProcessor (@RequestBody)
        ├─ RequestParamMethodArgumentResolver (@RequestParam)
        ├─ PathVariableMethodArgumentResolver (@PathVariable)
        ├─ ServletModelAttributeMethodProcessor (@ModelAttribute / sem anotação)
        └─ outros resolvers...
        │
        ▼
Parâmetros do método resolvidos
        │
        ▼
Método do Controller executado
```

### A Interface HandlerMethodArgumentResolver

Cada resolver implementa esta interface:

```java
public interface HandlerMethodArgumentResolver {
    
    // Verifica se este resolver pode lidar com o parâmetro
    boolean supportsParameter(MethodParameter parameter);
    
    // Resolve o valor do parâmetro a partir da requisição
    Object resolveArgument(MethodParameter parameter, 
                           ModelAndViewContainer mavContainer,
                           NativeWebRequest webRequest, 
                           WebDataBinderFactory binderFactory) throws Exception;
}
```

### Ordem de Execução

O Spring MVC mantém uma lista ordenada de resolvers. Quando precisa resolver um parâmetro:

1. **Itera** sobre os resolvers na ordem registrada.
2. **Verifica** se `supportsParameter()` retorna `true` para o parâmetro atual.
3. **Executa** o método `resolveArgument()` do primeiro resolver que retornar `true`.
4. **Lança exceção** se nenhum resolver suportar o parâmetro.

Esta é a lógica que diferencia anotações como `@RequestBody`, `@RequestParam` e `@ModelAttribute`.

### Resolvers Principais

| Resolver | Anotação | Ativa quando | Origem dos dados |
|---|---|---|---|
| **RequestResponseBodyMethodProcessor** | `@RequestBody` | Tem `@RequestBody` | Body da requisição (InputStream) |
| **RequestParamMethodArgumentResolver** | `@RequestParam` | Tem `@RequestParam` ou parâmetro simples | Query string, form data |
| **PathVariableMethodArgumentResolver** | `@PathVariable` | Tem `@PathVariable` | Variáveis do caminho da URL |
| **ServletModelAttributeMethodProcessor** | `@ModelAttribute` ou nenhuma | Tem `@ModelAttribute` ou nenhuma anotação | Query params, form data, multipart |

---

## 2. Query String vs Body JSON: Por que são diferentes?

### A Pergunta Crítica

Se o Spring consegue preencher um objeto a partir de parâmetros de query string:

```http
POST /chat?pergunta=herança
```

Por que não consegue fazer o mesmo automaticamente com o body JSON?

```http
POST /chat
Content-Type: application/json

{
    "pergunta": "herança"
}
```

**A resposta está nos locais onde o Spring busca os dados.**

### Origem dos Dados

#### Query String e Form Data

```http
POST /chat?pergunta=herança&topico=oop
```

Estes dados estão disponíveis no `HttpServletRequest` via:

```java
String pergunta = request.getParameter("pergunta");      // "herança"
String topico = request.getParameter("topico");           // "oop"

Map<String, String[]> params = request.getParameterMap(); // acessível
```

O `WebDataBinder` (usado pelo `ServletModelAttributeMethodProcessor`) lê diretamente do `request.getParameterMap()` e faz o binding automático para os campos do objeto.

#### Body JSON

```http
POST /chat
Content-Type: application/json

{
    "pergunta": "herança"
}
```

Os dados JSON estão NO BODY DA REQUISIÇÃO, acessível via:

```java
InputStream inputStream = request.getInputStream(); // precisa ler
String jsonBody = readInputStream(inputStream);     // precisa deserializar
```

**O `WebDataBinder` não sabe como ler o body.**

Ele foi projetado para trabalhar com:
- Query parameters (`?chave=valor`)
- Form data (`application/x-www-form-urlencoded`)
- Multipart form data (`multipart/form-data`)

**Não foi projetado para ler e desserializar JSON do body.**

### Mecanismo Especial para Body JSON

Para ler o body JSON, o Spring MVC usa um mecanismo completamente diferente:

1. **RequestResponseBodyMethodProcessor** (resolver específico para `@RequestBody`)
2. Lê o `InputStream` da requisição
3. Usa um **HttpMessageConverter** apropriado (geralmente `MappingJackson2HttpMessageConverter`)
4. O converter usa **Jackson** para desserializar JSON em um objeto Java

```text
Body JSON
   │
   ▼
InputStream
   │
   ▼
HttpMessageConverter
   │
   ▼
Jackson (desserializa JSON)
   │
   ▼
Objeto Java (PerguntaDTO)
```

---

## 3. Cenários Práticos: Resolução de Argumentos

### Cenário A: Sem anotação (Fallback para @ModelAttribute)

```java
@PostMapping
public RespostaDTO perguntar(PerguntaDTO pergunta) {
    // ...
}
```

**Requisição:**
```http
POST /chat?pergunta=herança
```

**O que acontece:**

1. O Spring procura um resolver que suporte `PerguntaDTO pergunta`.
2. Nenhum resolver específico é ativado (sem `@RequestBody`, `@RequestParam`, `@PathVariable`).
3. **Fallback:** `ServletModelAttributeMethodProcessor` toma conta (comportamento implícito de `@ModelAttribute`).
4. Cria `new PerguntaDTO()` (construtor padrão).
5. `WebDataBinder` tenta popular os campos:
   - Lê `request.getParameter("pergunta")` → encontra `"herança"`
   - Faz bind: `pergunta.setPergunta("herança")` ✅
6. Método é executado com `PerguntaDTO(pergunta="herança")` ✅

**Resultado:** ✅ FUNCIONA

---

### Cenário B: Sem anotação, mas COM body JSON

```java
@PostMapping
public RespostaDTO perguntar(PerguntaDTO pergunta) {
    // ...
}
```

**Requisição:**
```http
POST /chat
Content-Type: application/json

{
    "pergunta": "herança"
}
```

**O que acontece:**

1. O Spring procura um resolver que suporte `PerguntaDTO pergunta`.
2. Nenhum resolver específico é ativado.
3. **Fallback:** `ServletModelAttributeMethodProcessor` toma conta.
4. Cria `new PerguntaDTO()` (construtor padrão).
5. `WebDataBinder` tenta popular os campos:
   - Chama `request.getParameter("pergunta")` → retorna `null` (não há query params)
   - `WebDataBinder` NÃO lê o body JSON
   - O objeto permanece com `pergunta = null`
6. Método é executado com `PerguntaDTO(pergunta=null)` ❌

**Resultado:** ❌ NÃO FUNCIONA — Objeto vazio!

**Motivo:** O `ServletModelAttributeMethodProcessor` não foi projetado para ler o body. Ele só trabalha com parâmetros de requisição.

---

### Cenário C: Com @RequestBody

```java
@PostMapping
public RespostaDTO perguntar(@RequestBody PerguntaDTO pergunta) {
    // ...
}
```

**Requisição:**
```http
POST /chat
Content-Type: application/json

{
    "pergunta": "herança"
}
```

**O que acontece:**

1. O Spring procura um resolver que suporte `@RequestBody PerguntaDTO pergunta`.
2. **RequestResponseBodyMethodProcessor** retorna `true` (tem `@RequestBody`).
3. Este resolver:
   - Lê o `InputStream` do body
   - Consulta o `Content-Type: application/json`
   - Seleciona `MappingJackson2HttpMessageConverter`
   - Jackson desserializa JSON → `PerguntaDTO(pergunta="herança")`
4. Método é executado com o objeto corretamente preenchido ✅

**Resultado:** ✅ FUNCIONA

---

### Cenário D: Com @ModelAttribute Explícito

```java
@PostMapping
public RespostaDTO perguntar(@ModelAttribute PerguntaDTO pergunta) {
    // ...
}
```

**Requisição (com body JSON):**
```http
POST /chat
Content-Type: application/json

{
    "pergunta": "herança"
}
```

**O que acontece:**

1. O Spring reconhece `@ModelAttribute`.
2. **ServletModelAttributeMethodProcessor** é ativado.
3. Cria e tenta popular via `WebDataBinder`.
4. `WebDataBinder` NÃO lê o body JSON (por design).
5. Objeto fica vazio.

**Resultado:** ❌ NÃO FUNCIONA — `@ModelAttribute` não é para body JSON!

**Caso de uso correto para @ModelAttribute:**
```http
POST /chat?pergunta=herança&topico=oop
```
Ou form data.

---

### Cenário E: Com @RequestParam

```java
@PostMapping
public RespostaDTO perguntar(@RequestParam String pergunta) {
    // ...
}
```

**Requisição:**
```http
POST /chat?pergunta=herança
```

**O que acontece:**

1. O Spring reconhece `@RequestParam`.
2. **RequestParamMethodArgumentResolver** é ativado.
3. Extrai `request.getParameter("pergunta")` → `"herança"`.
4. Método é executado com `pergunta="herança"` ✅

**Resultado:** ✅ FUNCIONA

**Com body JSON:**
```http
POST /chat
Content-Type: application/json

{
    "pergunta": "herança"
}
```

**Resultado:** ❌ NÃO FUNCIONA — `@RequestParam` não lê body!

---

## 4. Tabela Comparativa: Anotações e Comportamento

| Assinatura do Método | Query String? | Body JSON? | Form Data? | Resolver Ativado | Resultado |
|---|---|---|---|---|---|
| `perguntar(PerguntaDTO p)` | ✅ | ❌ | ✅ | ServletModelAttributeMethodProcessor (implícito) | Popula de query/form, ignora JSON |
| `perguntar(@RequestBody PerguntaDTO p)` | ❌ | ✅ | ❌ | RequestResponseBodyMethodProcessor | Desserializa JSON ✅ |
| `perguntar(@ModelAttribute PerguntaDTO p)` | ✅ | ❌ | ✅ | ServletModelAttributeMethodProcessor (explícito) | Popula de query/form, ignora JSON |
| `perguntar(@RequestParam String p)` | ✅ | ❌ | ✅ | RequestParamMethodArgumentResolver | Extrai de parâmetros |
| `perguntar(@PathVariable String id)` | ✅ (path) | ❌ | ❌ | PathVariableMethodArgumentResolver | Extrai da URL |
| `perguntar(@RequestBody @Valid PerguntaDTO p)` | ❌ | ✅ | ❌ | RequestResponseBodyMethodProcessor + validação | Desserializa E valida ✅ |

---

## 5. Fluxo Completo: Como o Spring Resolve Cada Parâmetro

### Passo a Passo Detalhado

Considere este método:

```java
@PostMapping("/chat")
public RespostaDTO perguntar(
        @RequestBody PerguntaDTO pergunta,
        @RequestParam(required = false) String idioma) {
    // ...
}
```

**Requisição:**
```http
POST /chat?idioma=pt-BR
Content-Type: application/json

{
    "pergunta": "Explique herança"
}
```

### Execução (Passo a Passo)

```text
DispatcherServlet recebe a requisição
        │
        ▼
HandlerMapping encontra o método: perguntar(PerguntaDTO, String)
        │
        ▼
InvocableHandlerMethod prepara para invocar
        │
        ├─ Precisa resolver 2 parâmetros:
        │  1. @RequestBody PerguntaDTO pergunta
        │  2. @RequestParam(required=false) String idioma
        │
        ▼
Para o parâmetro 1 (@RequestBody PerguntaDTO):
        │
        ├─ HandlerMethodArgumentResolverComposite itera resolvers
        │
        ├─ RequestResponseBodyMethodProcessor.supportsParameter() → true (tem @RequestBody)
        │
        └─ Executa resolveArgument():
           ├─ Lê InputStream do body
           ├─ Detecta Content-Type: application/json
           ├─ Seleciona MappingJackson2HttpMessageConverter
           ├─ Jackson desserializa JSON
           └─ Retorna PerguntaDTO(pergunta="Explique herança")
        │
        ▼
Para o parâmetro 2 (@RequestParam String):
        │
        ├─ RequestParamMethodArgumentResolver.supportsParameter() → true (tem @RequestParam)
        │
        └─ Executa resolveArgument():
           ├─ Chama request.getParameter("idioma")
           ├─ Encontra "pt-BR"
           └─ Retorna "pt-BR"
        │
        ▼
Ambos os parâmetros resolvidos:
        pergunta = PerguntaDTO(pergunta="Explique herança")
        idioma = "pt-BR"
        │
        ▼
Invoca: perguntar(pergunta, idioma) ✅
```

---

## 6. Por que o Spring não automatiza body JSON sem @RequestBody?

### A Razão Arquitetural

Imagine se o Spring tentasse automaticamente desserializar qualquer objeto complexo do body sem anotação:

```java
@PostMapping
public RespostaDTO perguntar(PerguntaDTO pergunta) {
    // Ambíguo! É query param? É form data? É JSON no body?
}
```

**Problemas surgem:**

1. **Ambiguidade**: A mesma assinatura poderia significar diferentes coisas dependendo do `Content-Type`.

2. **Inconsistência**: Um desenvolvedor poderia enviar query params esperando que funcione como fallback para `@ModelAttribute`, mas outro esperaria que lesse body JSON.

3. **Erros silenciosos**: Objetos vazios seriam criados sem avisar o desenvolvedor. Bug difícil de rastrear.

4. **Performance**: O Spring teria que tentar desserializar o body mesmo quando não é necessário.

### A Solução: Anotações Explícitas

O Spring escolheu ser **explícito** em vez de implícito:

- `@RequestBody` → "Leia o body (JSON/XML/etc.)"
- `@RequestParam` → "Leia os parâmetros de query/form"
- `@ModelAttribute` → "Popule o objeto de query/form"
- Sem anotação (fallback) → "Trate como `@ModelAttribute`"

**Benefícios:**

✅ Clareza: O desenvolvedor sabe exatamente o que está acontecendo.

✅ Segurança: Erros de tipo são detectados em tempo de compilação (se usar tipos corretos).

✅ Flexibilidade: Combinar `@RequestBody` + `@RequestParam` é trivial e claro.

✅ Consistência: O mesmo código sempre se comporta da mesma forma.

---

## 7. Recomendações: Boas Práticas

### ✅ Faça Isso

```java
// 1. Sempre use @RequestBody para JSON
@PostMapping
public RespostaDTO perguntar(@RequestBody @Valid PerguntaDTO pergunta) {
    // ...
}

// 2. Combine @RequestBody + @RequestParam quando necessário
@PostMapping
public RespostaDTO perguntar(
        @RequestBody @Valid PerguntaDTO pergunta,
        @RequestParam String idioma) {
    // ...
}

// 3. Use @Valid para ativar validação
@PostMapping
public RespostaDTO perguntar(@RequestBody @Valid PerguntaDTO pergunta) {
    // Se pergunta for inválido, Spring retorna 400 Bad Request automaticamente
}

// 4. Use @RequestParam para parâmetros de query
@GetMapping
public List<PerguntaDTO> listar(@RequestParam(defaultValue = "0") int page) {
    // ...
}

// 5. Use @PathVariable para variáveis de URL
@GetMapping("/{id}")
public PerguntaDTO buscar(@PathVariable Long id) {
    // ...
}
```

### ❌ Não Faça Isso

```java
// ❌ Não confie no fallback implícito para body JSON
@PostMapping
public RespostaDTO perguntar(PerguntaDTO pergunta) {
    // Isso NÃO vai ler JSON do body!
}

// ❌ Não use @ModelAttribute para body JSON
@PostMapping
public RespostaDTO perguntar(@ModelAttribute PerguntaDTO pergunta) {
    // Isso cria um objeto vazio, ignorando o body
}

// ❌ Não misture @RequestParam com body JSON sem pensar
@PostMapping
public RespostaDTO perguntar(@RequestParam PerguntaDTO pergunta) {
    // @RequestParam não lê body, só query/form
}

// ❌ Não ignore validação
@PostMapping
public RespostaDTO perguntar(@RequestBody PerguntaDTO pergunta) {
    // Sem @Valid, um JSON malformado pode criar um objeto inválido
}
```

### 🎯 Checklist para Cada Endpoint

Ao criar um endpoint, verifique:

- [ ] **Content-Type da requisição**: É JSON? É form data? É query string?
- [ ] **Anotações corretas**: Usei `@RequestBody` para JSON?
- [ ] **Validação**: Adicionei `@Valid` se preciso validar entrada?
- [ ] **Defaults**: Há parâmetros opcionais? Usei `@RequestParam(required=false)`?
- [ ] **Documentação**: Exemplo de requisição está claro no código ou Javadoc?

---

## 8. Dúvidas Frequentes

### "Se PerguntaDTO pergunta (sem anotação) com body JSON não funciona, por que o Spring não lança erro imediatamente?"

O Spring **não** lança erro durante a resolução. Ele cria um objeto vazio:

```java
PerguntaDTO pergunta = new PerguntaDTO(); // pergunta = null
```

O erro pode acontecer depois, na camada de serviço:

```java
chatService.perguntar(pergunta.getPergunta()); // NullPointerException se pergunta for null
```

**Por isso validação é crucial:**
```java
@PostMapping
public RespostaDTO perguntar(@RequestBody @Valid PerguntaDTO pergunta) {
    // Spring retorna 400 antes mesmo de chamar o método
}
```

---

### "Por que o Spring suporta múltiplas formas de resolver argumentos?"

Porque HTTP suporta múltiplas formas de enviar dados:

- **Query string**: `GET /users?page=1`
- **Form data**: `POST /login` com `application/x-www-form-urlencoded`
- **JSON body**: `POST /chat` com `Content-Type: application/json`
- **Path variables**: `GET /users/{id}`
- **Headers**: `GET /resource` com `Authorization: Bearer ...`
- **Cookies**: `GET /profile` com cookies de sessão

Cada uma requer um mecanismo diferente. O Spring oferece um resolver para cada tipo.

---

### "A resolução de argumentos afeta a performance?"

Minimamente:

- A iteração sobre resolvers é rápida (geralmente 10-20 resolvers).
- O `supportsParameter()` faz apenas verificações de anotação (muito rápido).
- A desserialização JSON (Jackson) é a operação mais cara, não a resolução.

A performance real é limitada por I/O (ler do network) e processamento da lógica de negócio.

---

## Referência Rápida

### Quando usar cada anotação?

| Quando | Use | Exemplo |
|---|---|---|
| Dados no body (JSON) | `@RequestBody` | `@RequestBody PerguntaDTO pergunta` |
| Dados na query string | `@RequestParam` | `@RequestParam(required=false) String idioma` |
| Dados no path da URL | `@PathVariable` | `@PathVariable Long id` |
| Dados em query/form | `@ModelAttribute` | `@ModelAttribute FiltroDTO filtro` |
| Parâmetro simples, sem anotação | Fallback para `@ModelAttribute` | `String pergunta` (lê de query/form) |
| Dados no header | `@RequestHeader` | `@RequestHeader String authorization` |
| Dados em cookie | `@CookieValue` | `@CookieValue String sessionId` |

