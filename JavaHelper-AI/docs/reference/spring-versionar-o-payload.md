O **Versionamento de Payload** é uma estratégia para **evoluir sua API sem quebrar os clientes existentes** quando você precisa alterar a estrutura dos dados que trafegam no corpo (payload) das requisições/respostas.

Vou detalhar tudo com exemplos práticos em Spring:

---

## O que é Versionamento de Payload?

É a prática de **gerenciar diferentes versões do formato dos dados** que sua API aceita e retorna. Quando você precisa:
- Adicionar um novo campo obrigatório
- Renomear um campo existente
- Mudar o tipo de um campo (ex: String → Enum)
- Remover um campo obsoleto

Sem versionamento, você quebra todos os clientes que ainda usam o formato antigo.

---

## Por que versionar o Payload?

### Cenário real:
```java
// Versão 1 (LANÇADA)
public record ClienteRequestV1(
    String nome,
    String email
) {}

// Versão 2 (PRECISA EVOLUIR - 1 ano depois)
public record ClienteRequestV2(
    String nome,
    String email,
    String telefone,    // NOVO CAMPO OBRIGATÓRIO
    String documento    // NOVO CAMPO OBRIGATÓRIO
) {}
```

**Problema:** Se você simplesmente substituir V1 por V2, todos os apps mobile que ainda usam a V1 vão quebrar (e você não pode forçar todo mundo a atualizar).

**Solução:** Manter as duas versões simultaneamente!

---

## Estratégias de Versionamento no Spring

### 1️⃣ Versionamento por URL (Mais comum e recomendado)

```java
// CONTROLLER V1 - Mantido para clientes legados
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteV1Controller {
    
    @PostMapping
    public ResponseEntity<ClienteResponseV1> criar(@RequestBody ClienteRequestV1 payload) {
        // Apenas nome e email
        Cliente cliente = clienteService.criar(payload.nome(), payload.email());
        return ResponseEntity.ok(new ClienteResponseV1(cliente.getId(), cliente.getNome()));
    }
}

// CONTROLLER V2 - Nova versão com mais campos
@RestController
@RequestMapping("/api/v2/clientes")
public class ClienteV2Controller {
    
    @PostMapping
    public ResponseEntity<ClienteResponseV2> criar(@RequestBody ClienteRequestV2 payload) {
        // Nome, email, telefone e documento
        Cliente cliente = clienteService.criarCompleto(
            payload.nome(), 
            payload.email(), 
            payload.telefone(), 
            payload.documento()
        );
        return ResponseEntity.ok(new ClienteResponseV2(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getDocumento()
        ));
    }
}
```

**Como o cliente usa:**
```bash
# Cliente antigo (App versão 1.0)
POST https://api.exemplo.com/api/v1/clientes
{"nome": "João", "email": "joao@email.com"}

# Cliente novo (App versão 2.0)
POST https://api.exemplo.com/api/v2/clientes
{"nome": "João", "email": "joao@email.com", "telefone": "1199999999", "documento": "123456789"}
```

---

### 2️⃣ Versionamento por Header (Content-Type / Accept)

```java
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    // Versão 1 - Usa media type customizado
    @PostMapping(
        consumes = "application/vnd.meuv1+json",
        produces = "application/vnd.meuv1+json"
    )
    public ResponseEntity<ClienteResponseV1> criarV1(@RequestBody ClienteRequestV1 payload) {
        // ...
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.meuv1+json"))
            .body(response);
    }
    
    // Versão 2 - Media type diferente
    @PostMapping(
        consumes = "application/vnd.meuv2+json",
        produces = "application/vnd.meuv2+json"
    )
    public ResponseEntity<ClienteResponseV2> criarV2(@RequestBody ClienteRequestV2 payload) {
        // ...
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/vnd.meuv2+json"))
            .body(response);
    }
}
```

**Como o cliente usa:**
```bash
# Versão 1
POST /api/clientes
Content-Type: application/vnd.meuv1+json
{"nome": "João", "email": "joao@email.com"}

# Versão 2
POST /api/clientes
Content-Type: application/vnd.meuv2+json
{"nome": "João", "email": "joao@email.com", "telefone": "1199999999"}
```

---

### 3️⃣ Versionamento por Query Parameter (Menos recomendado)

```java
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @PostMapping
    public ResponseEntity<?> criar(
        @RequestParam(defaultValue = "1") int versao,
        @RequestBody Map<String, Object> payload
    ) {
        if (versao == 1) {
            // Processa V1
        } else if (versao == 2) {
            // Processa V2
        }
        // ...
    }
}
```

**Como o cliente usa:**
```bash
POST /api/clientes?versao=1
{"nome": "João", "email": "joao@email.com"}
```

⚠️ **Desvantagem:** Polui a URL e não é RESTful.

---

### 4️⃣ Versionamento Híbrido (Campos opcionais + Evolução gradual)

Essa é uma abordagem **mais elegante** que evita múltiplos controllers:

```java
// DTO único com todos os campos, mas com validação condicional
public record ClienteRequest(
    @NotBlank String nome,
    @NotBlank @Email String email,
    
    // Versão 2 - campos opcionais (não quebram clientes antigos)
    @Pattern(regexp = "\\d{10,11}") 
    String telefone,
    
    @CPF 
    String documento,
    
    // Campo para identificar a versão do cliente
    String versao
) {}

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest payload) {
        // Detecta versão pelo payload
        boolean isV2 = payload.telefone() != null || payload.documento() != null;
        
        if (isV2) {
            // Processa com todos os campos
            Cliente cliente = clienteService.criarCompleto(
                payload.nome(), 
                payload.email(), 
                payload.telefone(), 
                payload.documento()
            );
            return ResponseEntity.ok(new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getDocumento()
            ));
        } else {
            // Processa apenas V1
            Cliente cliente = clienteService.criar(payload.nome(), payload.email());
            return ResponseEntity.ok(new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                null,  // telefone
                null   // documento
            ));
        }
    }
}
```

---

## Boas Práticas para Versionamento

### 1. Deprecie versões antigas gradativamente
```java
@RestController
@RequestMapping("/api/v1/clientes")
@Deprecated // Marca como obsoleto
public class ClienteV1Controller {
    
    @PostMapping
    public ResponseEntity<ClienteResponseV1> criar(@RequestBody ClienteRequestV1 payload) {
        // Adiciona header avisando sobre depreciação
        return ResponseEntity.ok()
            .header("Deprecation", "true")
            .header("Sunset", "2026-12-31") // Data de desativação
            .body(response);
    }
}
```

### 2. Mantenha compatibilidade com transforms automáticos
```java
// Mapeador que converte V1 → V2 automaticamente
@Component
public class ClienteMapper {
    
    public ClienteRequestV2 toV2(ClienteRequestV1 v1) {
        return new ClienteRequestV2(
            v1.nome(),
            v1.email(),
            null,  // telefone - valor padrão
            null   // documento - valor padrão
        );
    }
    
    public ClienteResponseV1 toV1(ClienteResponseV2 v2) {
        return new ClienteResponseV1(
            v2.id(),
            v2.nome()
        );
    }
}
```

### 3. Documente as versões com OpenAPI
```java
@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes V1", description = "Versão legada - será descontinuada em 2026-12-31")
public class ClienteV1Controller { ... }

@RestController
@RequestMapping("/api/v2/clientes")
@Tag(name = "Clientes V2", description = "Versão atual com suporte a telefone e documento")
public class ClienteV2Controller { ... }
```

### 4. Use um interceptor para roteamento automático
```java
@Component
public class VersionInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Detecta versão pelo header ou URL
        String version = request.getHeader("X-API-Version");
        if (version == null) {
            version = "1"; // Default para compatibilidade
        }
        request.setAttribute("apiVersion", version);
        return true;
    }
}
```

---

## Quando NÃO versionar o Payload?

### ❌ Mudanças que são **backward-compatible** (não quebram clientes):
```java
// ANTES
public record UsuarioResponse(String nome) {}

// DEPOIS - Adicionar campo opcional NÃO quebra
public record UsuarioResponse(String nome, String email) {} 
// ✅ Clientes antigos ignoram o novo campo (se usar @JsonIgnoreProperties)
```

### ❌ Correções de bug que não alteram o contrato:
```java
// ANTES - Bug: data no formato errado
@JsonFormat(pattern = "dd/MM/yyyy")
LocalDate data;

// DEPOIS - Correção sem mudar o contrato
@JsonFormat(pattern = "yyyy-MM-dd") 
LocalDate data;
// ⚠️ Isso QUEBRA! Melhor criar V2
```

---

## Resumo: Qual estratégia escolher?

| Estratégia | Vantagens | Desvantagens | Quando usar |
|------------|-----------|--------------|-------------|
| **URL (/v1, /v2)** | Mais clara, fácil de documentar, cache separado | Polui a URL, duplicação de código | **Recomendado para APIs públicas** |
| **Header (Content-Type)** | Mais RESTful, URL limpa | Menos óbvio para desenvolvedores | APIs internas ou com muitos endpoints |
| **Campos opcionais** | Menos duplicação, evolução gradual | Pode ficar confuso com muitas versões | Mudanças pequenas e compatíveis |
| **Query Parameter** | Fácil de testar | Anti-pattern REST, polui URL | Apenas para protótipos |

---

## Exemplo Completo: Evolução de 3 Versões

```java
// V1 - Lançamento (2024)
public record PedidoRequestV1(
    @NotBlank String cliente,
    @Positive BigDecimal valor
) {}

// V2 - Adicionou endereço (2025)
public record PedidoRequestV2(
    @NotBlank String cliente,
    @Positive BigDecimal valor,
    String endereco  // Opcional para não quebrar V1
) {}

// V3 - Mudou cliente para objeto (2026)
public record PedidoRequestV3(
    ClienteDTO cliente,  // Agora é um objeto!
    @Positive BigDecimal valor,
    String endereco,
    List<ItemDTO> itens  // Nova funcionalidade
) {}

@RestController
public class PedidoController {
    
    @PostMapping("/api/v1/pedidos")
    public ResponseEntity<PedidoResponseV1> criarV1(@RequestBody PedidoRequestV1 payload) {
        return ok(new PedidoResponseV1(payload.cliente(), payload.valor()));
    }
    
    @PostMapping("/api/v2/pedidos")
    public ResponseEntity<PedidoResponseV2> criarV2(@RequestBody PedidoRequestV2 payload) {
        // Reusa lógica V1 + adiciona endereço
        Pedido pedido = criarPedidoBase(payload.cliente(), payload.valor());
        if (payload.endereco() != null) {
            pedido.setEndereco(payload.endereco());
        }
        return ok(new PedidoResponseV2(pedido));
    }
    
    @PostMapping("/api/v3/pedidos")
    public ResponseEntity<PedidoResponseV3> criarV3(@RequestBody PedidoRequestV3 payload) {
        // Lógica completamente nova
        return ok(pedidoService.criarPedidoCompleto(payload));
    }
}
```

---

## Conclusão

O **Versionamento de Payload** é essencial para:
- ✅ **Evoluir sua API** sem quebrar clientes existentes
- ✅ **Dar tempo** para clientes migrarem para novas versões
- ✅ **Manter compatibilidade** com apps mobile que não podem atualizar imediatamente
- ✅ **Reduzir riscos** em deploys de novas funcionalidades

A estratégia mais usada no mercado é **URL versioning (`/api/v1/...`, `/api/v2/...`)** porque é explícita, fácil de documentar e debuggar.

**Lembre-se:** Planeje suas versões com antecedência e sempre documente claramente as mudanças e prazos de depreciação!