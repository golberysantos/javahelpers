# HTTP: Conceitos e Contrato

Este documento apresenta uma explicação completa e detalhada sobre HTTP (HyperText Transfer Protocol), abrangendo fundamentos, métodos, status codes, headers, estrutura de requisições/respostas, autenticação, segurança e práticas recomendadas para desenvolvimento de APIs em Java.

## Sumário

- Introdução ao HTTP
- Características e versões
- Métodos HTTP
- Status Codes
- Headers HTTP
- Estrutura de Requisição e Resposta
- Content Negotiation e Content-Type
- Autenticação e Autorização
- CORS (Cross-Origin Resource Sharing)
- RESTful APIs e Contrato HTTP
- Caching e Performance
- Segurança
- Boas Práticas
- Exemplos em Java/Spring

## Introdução ao HTTP

HTTP é um protocolo de aplicação sem estado, baseado em cliente-servidor, projetado para transferência de dados (hipertexto, APIs, etc.) na web. É o fundamento das comunicações web modernas e é amplamente utilizado em arquiteturas de microsserviços, APIs RESTful e aplicações distribuídas.

### Características principais

- **Sem estado (Stateless)**: cada requisição é independente; o servidor não retém informações sobre requisições anteriores do cliente.
- **Baseado em requisição-resposta**: o cliente inicia uma requisição, o servidor processa e envia uma resposta.
- **Simples e extensível**: protocolo textual com estrutura clara, facilita debug e extensão via headers customizados.
- **Seguro via HTTPS**: HTTP sobre TLS/SSL fornece criptografia e autenticação de servidor.
- **Stateless facilita escalabilidade**: múltiplas instâncias de servidores podem servir requisições sem sincronização de sessão.

### Versões do HTTP

- **HTTP/1.0**: primeira versão padronizada; conexão por requisição (overhead).
- **HTTP/1.1**: mantém conexão aberta (keep-alive); permite pipelining; headers obrigatórios como `Host`; versão mais comum durante anos.
- **HTTP/2**: multiplexação de streams; compressão de headers (HPACK); push do servidor; melhor performance.
- **HTTP/3**: baseado em QUIC (protocolo de transporte); reduz latência; ainda em adoção.

## Métodos HTTP

Métodos HTTP definem a ação desejada sobre um recurso. Em APIs RESTful, mapeiam a semântica CRUD (Create, Read, Update, Delete).

### GET

- **Semantica**: recuperar dados de um recurso sem modificá-lo.
- **Idempotente**: sim (múltiplas chamadas retornam o mesmo resultado).
- **Corpo da requisição**: não deve conter corpo; parâmetros na query string.
- **Cache**: respostas podem ser cacheadas por padrão.
- **Exemplos**:
  - `GET /api/users/123` → retorna usuário com ID 123
  - `GET /api/users?page=1&limit=10` → lista paginada de usuários

### POST

- **Semântica**: criar um novo recurso ou iniciar uma ação.
- **Idempotente**: não (múltiplas chamadas podem criar múltiplos recursos).
- **Corpo da requisição**: obrigatório; contém dados do novo recurso (JSON, XML, form-encoded).
- **Status esperado**: 201 (Created) com Location header apontando para o novo recurso.
- **Exemplos**:
  - `POST /api/users` com corpo `{ "name": "João", "email": "joao@example.com" }` → cria novo usuário
  - `POST /api/orders/123/payments` → inicia processamento de pagamento

### PUT

- **Semântica**: substituir completamente um recurso existente.
- **Idempotente**: sim (múltiplas chamadas resultam no mesmo estado do recurso).
- **Corpo da requisição**: obrigatório; representa o estado completo do recurso.
- **Status esperado**: 200 (OK) ou 204 (No Content).
- **Características**: exige que o cliente envie **todos** os campos; se omitir um campo, pode ser deletado ou rejeitado.
- **Exemplos**:
  - `PUT /api/users/123` com corpo completo do usuário → substitui usuário 123 integralmente

### PATCH

- **Semântica**: aplicar modificações parciais a um recurso.
- **Idempotente**: pode ser idempotente (depende da implementação).
- **Corpo da requisição**: obrigatório; contém apenas os campos a serem modificados.
- **Status esperado**: 200 (OK) ou 204 (No Content).
- **Características**: JSON Merge Patch (RFC 7386) ou JSON Patch (RFC 6902) são abordagens comuns.
- **Exemplos**:
  - `PATCH /api/users/123` com corpo `{ "email": "novo@example.com" }` → atualiza apenas email

### DELETE

- **Semântica**: remover um recurso.
- **Idempotente**: sim (deletar múltiplas vezes tem o mesmo efeito final).
- **Corpo da requisição**: geralmente vazio; alguns servidores aceitam corpo explicativo.
- **Status esperado**: 204 (No Content) ou 200 (OK) com body explicativo.
- **Exemplos**:
  - `DELETE /api/users/123` → remove usuário 123

### Outros métodos

- **HEAD**: como GET, mas retorna apenas headers (sem corpo de resposta); útil para verificar disponibilidade/tamanho.
- **OPTIONS**: retorna métodos HTTP permitidos em um recurso; usado para CORS preflight.
- **CONNECT**: estabelece túnel (proxy); usado em HTTPS.
- **TRACE**: eco da requisição (debugging); raramente usado em produção.

## Status Codes

Status codes indicam o resultado de uma requisição HTTP. Agrupados em classes:

### 1xx - Informational

- **100 Continue**: cliente pode enviar o corpo da requisição.
- **101 Switching Protocols**: servidor concorda em trocar protocolo (ex: WebSocket).

### 2xx - Success

- **200 OK**: requisição bem-sucedida; corpo contém a resposta.
- **201 Created**: recurso criado com sucesso; `Location` header contém URL do novo recurso.
- **202 Accepted**: requisição aceita para processamento assíncrono.
- **204 No Content**: sucesso, mas sem corpo de resposta (comum em DELETE, PUT, PATCH).
- **206 Partial Content**: resposta parcial (usado em downloads com range requests).

### 3xx - Redirection

- **300 Multiple Choices**: múltiplas opções de representação disponíveis.
- **301 Moved Permanently**: recurso movido permanentemente; cliente deve atualizar URL.
- **302 Found**: redirecionamento temporário (mudança comum em HTTP/1.1).
- **304 Not Modified**: resposta cacheada é válida; cliente usa versão local.
- **307 Temporary Redirect**: redirecionamento temporário (preserva método HTTP).

### 4xx - Client Error

- **400 Bad Request**: requisição malformada ou inválida.
- **401 Unauthorized**: autenticação necessária ou falhou.
- **403 Forbidden**: autenticado, mas sem permissão para acessar recurso.
- **404 Not Found**: recurso não encontrado.
- **405 Method Not Allowed**: método HTTP não permitido neste recurso.
- **409 Conflict**: conflito (ex: versão desatualizada em PUT/PATCH).
- **422 Unprocessable Entity**: semântica da requisição é válida, mas dados são inválidos.
- **429 Too Many Requests**: limite de taxa excedido (rate limiting).

### 5xx - Server Error

- **500 Internal Server Error**: erro genérico no servidor.
- **502 Bad Gateway**: gateway/proxy recebeu resposta inválida.
- **503 Service Unavailable**: servidor temporariamente indisponível.
- **504 Gateway Timeout**: gateway/proxy timeout na resposta.

## Headers HTTP

Headers HTTP transmitem metadados sobre a requisição ou resposta.

### Headers comuns de requisição

- **Host**: domínio e porta do servidor (obrigatório em HTTP/1.1).
- **User-Agent**: identificação do cliente (navegador, biblioteca, versão).
- **Accept**: tipos de mídia aceitos na resposta (ex: `application/json, application/xml`).
- **Accept-Encoding**: compressão aceita (ex: `gzip, deflate`).
- **Accept-Language**: idiomas preferidos (ex: `pt-BR, en-US`).
- **Content-Type**: tipo de mídia do corpo (ex: `application/json`).
- **Content-Length**: tamanho do corpo em bytes.
- **Authorization**: credenciais de autenticação (ex: `Bearer <token>`).
- **Cookie**: cookies armazenados para o domínio.
- **Referer**: URL da página anterior (navegação).
- **X-Requested-With**: identifica requisições AJAX (ex: `XMLHttpRequest`).

### Headers comuns de resposta

- **Content-Type**: tipo de mídia do corpo de resposta.
- **Content-Length**: tamanho do corpo em bytes.
- **Content-Encoding**: compressão aplicada ao corpo (ex: `gzip`).
- **Cache-Control**: diretivas de cache (ex: `no-cache, max-age=3600`).
- **ETag**: identificador de versão (para validação de cache).
- **Last-Modified**: data/hora da última modificação do recurso.
- **Location**: URL de redirecionamento (3xx) ou novo recurso (201).
- **Set-Cookie**: armazena cookies no cliente.
- **Access-Control-Allow-Origin**: permite CORS (ex: `https://example.com`).
- **Access-Control-Allow-Methods**: métodos HTTP permitidos (CORS).
- **Access-Control-Allow-Headers**: headers customizados permitidos (CORS).
- **WWW-Authenticate**: desafio de autenticação (401 Unauthorized).
- **X-Frame-Options**: controla incorporação em frames (segurança).
- **X-Content-Type-Options**: previne MIME sniffing (ex: `nosniff`).

### Headers customizados

- Prefixo `X-` é comum para headers customizados (ex: `X-Request-ID`, `X-API-Version`).
- Headers customizados não devem alterar semântica do protocolo.

## Estrutura de Requisição e Resposta

### Requisição HTTP

```
GET /api/users/123 HTTP/1.1
Host: api.example.com
User-Agent: curl/7.64.1
Accept: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

[corpo vazio para GET]
```

**Partes**:
1. **Linha de requisição**: `MÉTODO CAMINHO HTTP/VERSÃO`
2. **Headers**: `Nome: Valor` (um por linha)
3. **Linha em branco**: separa headers do corpo
4. **Corpo**: dados da requisição (JSON, form-encoded, etc.); vazio em GET

### Resposta HTTP

```
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 156
Cache-Control: max-age=3600
ETag: "abc123"

{
  "id": 123,
  "name": "João Silva",
  "email": "joao@example.com",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Partes**:
1. **Linha de status**: `HTTP/VERSÃO CÓDIGO MOTIVO`
2. **Headers**: metadados da resposta
3. **Linha em branco**: separa headers do corpo
4. **Corpo**: dados da resposta (JSON, HTML, etc.); pode estar vazio

## Content Negotiation e Content-Type

Content negotiation permite cliente e servidor concordarem sobre formato de dados.

### Content-Type

- **application/json**: JavaScript Object Notation; padrão em APIs modernas.
- **application/xml**: XML; formato estruturado e verboso.
- **application/x-www-form-urlencoded**: dados de formulário URL-encoded.
- **multipart/form-data**: upload de arquivos; dados e binários misturados.
- **text/plain**: texto simples.
- **text/html**: HTML; usado em respostas web.
- **application/octet-stream**: binário genérico; download de arquivo.

### Negociação via Accept

Cliente indica preferência:

```
Accept: application/json, application/xml;q=0.9, */*;q=0.8
```

Interpre: JSON é preferido (q=1.0 implícito), XML é aceitável (q=0.9), qualquer outro é menos desejável (q=0.8).

Servidor responde com `Content-Type` indicando formato escolhido.

## Autenticação e Autorização

### Autenticação

Autenticação verifica **quem você é**. Mecanismos comuns:

- **Basic Auth**: credenciais em Base64 no header `Authorization: Basic <base64(user:pass)>`. Inseguro sem HTTPS.
- **Bearer Token**: token opaco no header `Authorization: Bearer <token>`. Usado em OAuth2, JWT.
- **API Key**: chave no header ou query string. Simples, menos flexível.
- **OAuth2/OpenID Connect**: delegação de autenticação a provedor central (Google, GitHub, Keycloak).
- **Mutual TLS (mTLS)**: certificados de cliente e servidor; autenticação bidirecional.

### Autorização

Autorização determina **o que você pode fazer**. Mecanismos:

- **Role-based Access Control (RBAC)**: usuário tem roles (admin, user, guest); cada role tem permissões.
- **Attribute-based Access Control (ABAC)**: decisão baseada em atributos (usuário, recurso, contexto).
- **Scope (OAuth2)**: token limitado a escopos específicos (ex: `read:users`, `write:orders`).

## CORS (Cross-Origin Resource Sharing)

CORS permite requisições cross-origin (de domínio diferente) de forma segura.

### Política Same-Origin

Por padrão, requisições AJAX só funcionam se cliente e servidor têm **origem idêntica** (scheme + host + port).

```
https://app.example.com:443/page → https://api.example.com:443/data ❌ Bloqueado (host diferente)
https://app.example.com:443/page → https://app.example.com:443/api ✅ Permitido (mesma origem)
```

### CORS Handshake

1. **Preflight (OPTIONS)**: navegador envia requisição OPTIONS para verificar se servidor permite cross-origin.

```
OPTIONS /api/users HTTP/1.1
Origin: https://app.example.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: Content-Type
```

2. **Resposta do servidor**: informa origens, métodos e headers permitidos.

```
HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://app.example.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Max-Age: 86400
```

3. **Requisição real**: navegador envia a requisição real se preflight passou.

### Headers CORS importantes

- **Access-Control-Allow-Origin**: origem(ns) permitidas (ex: `*` para qualquer, `https://app.example.com` específica).
- **Access-Control-Allow-Methods**: métodos HTTP permitidos.
- **Access-Control-Allow-Headers**: headers customizados permitidos.
- **Access-Control-Allow-Credentials**: `true` permite enviar cookies com requisição cross-origin.
- **Access-Control-Max-Age**: tempo (segundos) que resposta preflight é cacheada.

## RESTful APIs e Contrato HTTP

### Princípios REST

REST (Representational State Transfer) é uma arquitetura para APIs web. Princípios:

1. **Cliente-Servidor**: separação de responsabilidades.
2. **Stateless**: servidor não mantém contexto de cliente.
3. **Cache**: respostas podem ser cacheadas para melhorar performance.
4. **Interface uniforme**: recursos identificáveis, manipuláveis via representações, métodos padrão.
5. **Sistema em camadas**: cliente não conhece se está conectado a servidor final ou intermediário.
6. **Código sob demanda** (opcional): servidor pode estender funcionalidade do cliente.

### Recursos e URIs

- **Recursos**: entidades de negócio (usuários, pedidos, produtos).
- **URIs**: identificam recursos de forma única e previsível.
- **Hierarquia**: `/api/companies/123/departments/456/employees/789`

### Contrato HTTP em APIs RESTful

```
GET /api/users               → listar usuários
POST /api/users              → criar novo usuário
GET /api/users/123           → obter usuário 123
PUT /api/users/123           → substituir usuário 123 completamente
PATCH /api/users/123         → atualizar parcialmente usuário 123
DELETE /api/users/123        → deletar usuário 123

GET /api/users/123/orders    → listar pedidos do usuário 123
POST /api/users/123/orders   → criar novo pedido para usuário 123
```

### Versionamento

- **URL Path**: `/api/v1/users`, `/api/v2/users` (explícito, fácil roteamento).
- **Query String**: `/api/users?version=2` (menos comum).
- **Header customizado**: `X-API-Version: 2` (transparente em URL).
- **Accept Header**: `Accept: application/vnd.api+json;version=2` (content negotiation).

## Caching e Performance

### Cache-Control

Diretiva `Cache-Control` controla cache em cliente e proxy:

```
Cache-Control: public, max-age=3600, must-revalidate
```

- **public**: qualquer cache pode armazenar.
- **private**: apenas cache do cliente (navegador) pode armazenar.
- **max-age**: tempo em segundos antes de expirar.
- **must-revalidate**: após expiração, deve revalidar com servidor.
- **no-cache**: valida sempre com servidor antes de usar.
- **no-store**: não cache.
- **immutable**: conteúdo nunca muda; cache indefinidamente.

### ETag e Conditional Requests

- **ETag**: hash do corpo da resposta; identifica versão específica.
- **If-None-Match**: cliente envia ETag anterior; servidor retorna 304 se não mudou.

```
GET /api/users/123
If-None-Match: "abc123"

HTTP/1.1 304 Not Modified
```

### Compressão

Servidor comprime resposta; cliente descompacta:

```
GET /api/users
Accept-Encoding: gzip, deflate

HTTP/1.1 200 OK
Content-Encoding: gzip
Content-Length: 1024
```

## Segurança

### HTTPS (HTTP Secure)

- Obrigatório em produção.
- TLS/SSL criptografa dados em trânsito.
- Previne man-in-the-middle, eavesdropping.
- Certificados validam identidade do servidor.

### Segurança de Headers

- **X-Content-Type-Options: nosniff**: previne MIME sniffing attacks.
- **X-Frame-Options: DENY**: previne clickjacking (impede incorporação em iframe).
- **X-XSS-Protection: 1; mode=block**: ativa proteção contra XSS (navegadores).
- **Strict-Transport-Security**: força HTTPS (ex: `max-age=31536000; includeSubDomains`).
- **Content-Security-Policy**: controla recursos que página pode carregar.

### Rate Limiting

Protege servidor contra abuso:

```
HTTP/1.1 429 Too Many Requests
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1629820800
```

### Validação de Entrada

- Validar todos os dados de entrada (tipo, tamanho, formato).
- Sanitizar antes de usar em queries (SQL injection, NoSQL injection).
- Rejeitar requisições malformadas (400 Bad Request).

## Boas Práticas

1. **Use métodos HTTP corretamente**: GET para leitura, POST para criação, PUT/PATCH para atualização, DELETE para remoção.
2. **Idempotência**: operações repetidas não têm efeitos colaterais (GET, PUT, DELETE).
3. **Status codes semânticos**: retorne status codes apropriados (200, 201, 400, 404, 500).
4. **Versionamento de API**: versione sua API para manter compatibilidade.
5. **Documentação clara**: OpenAPI/Swagger descreve endpoints, parâmetros, respostas.
6. **Paginação**: implemente paginação para endpoints que retornam muitos dados.
   ```
   GET /api/users?page=1&limit=20&sort=name
   ```
7. **Filtros e busca**: permita filtrar e buscar recursos.
   ```
   GET /api/users?role=admin&status=active
   ```
8. **Rate limiting**: proteja contra abuso.
9. **Logging e monitoramento**: registre requisições, respostas, erros.
10. **Segurança**: use HTTPS, valide entrada, implemente autenticação/autorização.
11. **Tratamento de erro**: respostas de erro com mensagens claras.
    ```json
    {
      "error": "validation_error",
      "message": "Email is invalid",
      "details": [
        { "field": "email", "code": "invalid_format" }
      ]
    }
    ```
12. **Content negotiation**: suporte múltiplos formatos (JSON, XML).

## Exemplos em Java/Spring

### GET - Recuperar Recurso

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    public ResponseEntity<Page<UserDto>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String role) {
        Page<UserDto> users = userService.list(page, size, role);
        return ResponseEntity.ok(users);
    }
}
```

### POST - Criar Recurso

```java
@PostMapping
public ResponseEntity<UserDto> createUser(@RequestBody @Valid CreateUserCmd cmd) {
    UserDto user = userService.create(cmd);
    return ResponseEntity
        .created(URI.create("/api/users/" + user.getId()))
        .body(user);
}
```

### PUT - Substituir Recurso

```java
@PutMapping("/{id}")
public ResponseEntity<UserDto> updateUser(
        @PathVariable Long id,
        @RequestBody @Valid UpdateUserCmd cmd) {
    UserDto user = userService.update(id, cmd);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
}
```

### PATCH - Atualização Parcial

```java
@PatchMapping("/{id}")
public ResponseEntity<UserDto> patchUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> updates) {
    UserDto user = userService.patch(id, updates);
    if (user == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
}
```

### DELETE - Remover Recurso

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    boolean deleted = userService.delete(id);
    if (!deleted) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
}
```

### Tratamento de Erro Global

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<Map<String, String>> details = fieldErrors.stream()
            .map(e -> Map.of("field", e.getField(), "code", e.getCode()))
            .collect(Collectors.toList());
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", "Invalid request", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
```

### CORS Configuration (Spring Boot)

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("https://app.example.com")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
```

### Rate Limiting com Spring Cloud Sleuth

```java
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    
    private final RateLimiter rateLimiter = RateLimiter.create(10.0); // 10 requisições/segundo
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                            Object handler) throws Exception {
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded");
            return false;
        }
        return true;
    }
}
```

---

## Conclusão

HTTP é o protocolo fundamental para comunicação web moderna. Entender seus conceitos — métodos, status codes, headers, autenticação, segurança — é essencial para desenvolver APIs robustas, seguras e escaláveis em Java. Ao seguir princípios RESTful e boas práticas, você constrói contratos HTTP claros e previsíveis que facilitam integração, manutenção e evolução de sistemas distribuídos.
