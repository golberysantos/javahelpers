# Boas Práticas com Payload no Spring Framework

> **Payload** = Corpo da requisição/resposta HTTP ou conteúdo principal de uma mensagem em filas/eventos.  
> Este guia foca em boas práticas para lidar com payloads de forma segura, performática e maintainable em aplicações Spring Boot. Ele cobre desde fundamentos até tópicos avançados como auditoria, testes de contrato e anti-patterns. 


---

## Sumário
1. [Use DTOs, Nunca Entidades](#1-use-dtos-nunca-entidades)
2. [Validação Robusta com Bean Validation](#2-validação-robusta-com-bean-validation)
3. [Versionamento de Payload](#3-versionamento-de-payload)
4. [Limites de Tamanho e Segurança](#4-limites-de-tamanho-e-segurança)
5. [Serialização/Desserialização Eficiente](#5-serializaçãodesserialização-eficiente)
6. [Tratamento de Erros e Mensagens Claras](#6-tratamento-de-erros-e-mensagens-claras)
7. [Payload em Filas (RabbitMQ/Kafka)](#7-payload-em-filas-rabbitmqkafka)
8. [Logging Estratégico de Payloads](#8-logging-estratégico-de-payloads)
9. [Payload em Multipart/File Upload](#9-payload-em-multipartfile-upload)
10. [Testes e Documentação](#10-testes-e-documentação)
11. [Anti-Patterns a Evitar](#11-anti-patterns-a-evitar)

---

## 1. Use DTOs, Nunca Entidades

### ❌ Ruim
```java
@PostMapping("/usuarios")
public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) { // Entidade JPA exposta!
    return ResponseEntity.ok(usuarioService.salvar(usuario));
}
```
**Problemas:**  
- Exposição de campos internos (senha, data de criação, etc.)  
- Acoplamento da API com o esquema do banco de dados  
- Dificuldade para evoluir a API sem quebrar o banco  

### ✅ Bom
```java
// DTO de entrada (Request)
public record UsuarioRequestDTO(
    @NotBlank String nome,
    @Email String email,
    @Size(min = 8) String senha
) {}

// DTO de saída (Response)
public record UsuarioResponseDTO(
    Long id,
    String nome,
    String email,
    Instant dataCriacao
) {}

@PostMapping("/usuarios")
public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO payload) {
    Usuario usuario = usuarioMapper.toEntity(payload);
    Usuario salvo = usuarioService.salvar(usuario);
    return ResponseEntity.ok(usuarioMapper.toResponseDTO(salvo));
}
```

**Vantagens:**  
- Controle total sobre o que entra e sai  
- Separação clara entre camada web e persistência  
- Facilita evolução da API sem impactar o banco  

---

## 2. Validação Robusta com Bean Validation

### Validações básicas
```java
public record PedidoRequestDTO(
    @NotNull(message = "ID do cliente é obrigatório") Long clienteId,
    @NotEmpty(message = "Itens não podem estar vazios") List<@Valid ItemPedidoDTO> itens,
    @Positive(message = "Total deve ser positivo") BigDecimal total,
    @PastOrPresent(message = "Data não pode ser futura") Instant data
) {}
```

### Validações condicionais com grupos
```java
public interface ValidationGroups {
    interface OnCreate {}
    interface OnUpdate {}
}

public record ProdutoDTO(
    @Null(groups = OnCreate.class, message = "ID não deve ser enviado na criação")
    @NotNull(groups = OnUpdate.class, message = "ID é obrigatório na atualização")
    Long id,
    
    @NotBlank String nome,
    @DecimalMin("0.01") BigDecimal preco
) {}

// No controller
@PostMapping
public ResponseEntity criar(@Validated(ValidationGroups.OnCreate.class) @RequestBody ProdutoDTO payload) { ... }

@PutMapping
public ResponseEntity atualizar(@Validated(ValidationGroups.OnUpdate.class) @RequestBody ProdutoDTO payload) { ... }
```

### Validação customizada
```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfValidator.class)
public @interface CPF {
    String message() default "CPF inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class CpfValidator implements ConstraintValidator<CPF, String> {
    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }
}
```

---

## 3. Versionamento de Payload
O Versionamento de Payload é uma estratégia para evoluir sua API sem quebrar os clientes existentes quando você precisa alterar a estrutura dos dados que trafegam no corpo (payload) das requisições/respostas.
É a prática de gerenciar diferentes versões do formato dos dados que sua API aceita e retorna. Quando você precisa:
- Adicionar um novo campo obrigatório
- Renomear um campo existente
- Mudar o tipo de um campo (ex: String → Enum)
- Remover um campo obsoleto
Sem versionamento, você quebra todos os clientes que ainda usam o formato antigo.

### Estratégia 1: Versionamento por URL
```java
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoV1Controller {
    @PostMapping
    public ResponseEntity<PedidoResponseV1> criar(@RequestBody PedidoRequestV1 payload) { ... }
}

@RestController
@RequestMapping("/api/v2/pedidos")
public class PedidoV2Controller {
    @PostMapping
    public ResponseEntity<PedidoResponseV2> criar(@RequestBody PedidoRequestV2 payload) { ... }
}
```

### Estratégia 2: Versionamento por Header (Content-Type)
```java
@PostMapping(value = "/pedidos", consumes = "application/vnd.meuv1+json")
public ResponseEntity criarV1(@RequestBody PedidoRequestV1 payload) { ... }

@PostMapping(value = "/pedidos", consumes = "application/vnd.meuv2+json")
public ResponseEntity criarV2(@RequestBody PedidoRequestV2 payload) { ... }
```

### Estratégia 3: Campos opcionais com fallback (evitar quebrar clientes)
```java
public record UsuarioUpdateRequest(
    String nome,  // Opcional - se null, não altera
    String email, // Opcional
    @JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos novos
    Map<String, Object> extras // Para flexibilidade futura
) {}
```

---

## 4. Limites de Tamanho e Segurança

### Configurações no `application.yml`
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 5MB        # Tamanho máximo de arquivo
      max-request-size: 10MB    # Tamanho máximo da requisição inteira
  codec:
    max-in-memory-size: 5MB     # Tamanho máximo em memória para desserialização

server:
  tomcat:
    max-http-form-post-size: 10MB  # Limite para POST com form-data
```

### Proteção contra DoS (Ataque de Bomba Zip / Billion Laughs)
```java
@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.featuresToEnable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        builder.featuresToDisable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        
        // Limita profundidade de aninhamento para evitar ataques
        builder.postConfigurer(objectMapper -> {
            objectMapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
            // Define limite de profundidade de 1000 (valor razoável)
            objectMapper.getFactory().setCharacterEscapes(StandardCharsets.UTF_8);
        });
        return builder;
    }
}
```

### Validação manual de tamanho para payloads JSON
```java
@Component
public class PayloadSizeFilter extends OncePerRequestFilter {
    @Value("${app.payload.max-size-bytes:1048576}") // 1MB default
    private long maxPayloadSize;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
        chain.doFilter(wrapper, response);
        
        if (wrapper.getContentAsByteArray().length > maxPayloadSize) {
            throw new PayloadTooLargeException("Payload excede o limite de " + maxPayloadSize + " bytes");
        }
    }
}
```

---

## 5. Serialização/Desserialização Eficiente

### Use Records (Java 17+) para DTOs imutáveis
```java
// Mais performático, thread-safe e com equals/hashCode já implementados
public record ProdutoRequest(
    @NotBlank String sku,
    @NotBlank String nome,
    @Positive BigDecimal preco,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataValidade
) {}
```

### Configuração avançada do ObjectMapper
```java
@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Tolerância a campos desconhecidos (não quebrar com evoluções)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Datas no padrão ISO-8601
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Pretty print apenas em desenvolvimento
        if (environment.acceptsProfiles("dev")) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        
        // Tratamento de enums por nome (mais seguro que ordinal)
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        
        return mapper;
    }
}
```

### Desserialização customizada com @JsonCreator
```java
public record EnderecoDTO(
    String logradouro,
    String numero,
    String complemento
) {
    @JsonCreator
    public EnderecoDTO(
        @JsonProperty("rua") String logradouro,  // Mapeia campo "rua" do JSON
        @JsonProperty("num") String numero,
        @JsonProperty("comp") String complemento
    ) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
    }
}
```

---

## 6. Tratamento de Erros e Mensagens Claras

### Global Exception Handler com mensagens detalhadas
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
            
        return ResponseEntity.badRequest().body(new ErroResponse(
            "ERRO_VALIDACAO",
            "Payload inválido",
            erros,
            LocalDateTime.now()
        ));
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(
            "ERRO_JSON",
            "Payload mal formatado. Verifique a estrutura JSON.",
            List.of(ex.getMostSpecificCause().getMessage()),
            LocalDateTime.now()
        ));
    }
    
    @ExceptionHandler(ConstraintViolationException.class) // Para validações em @RequestParam
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErroResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(
            "ERRO_PARAMETRO",
            "Parâmetro inválido",
            ex.getConstraintViolations().stream().map(v -> v.getMessage()).collect(Collectors.toList()),
            LocalDateTime.now()
        ));
    }
}

public record ErroResponse(
    String codigo,
    String mensagem,
    List<String> detalhes,
    LocalDateTime timestamp
) {}
```

---

## 7. Payload em Filas (RabbitMQ/Kafka)

### Use DTOs serializáveis para mensagens
```java
// No produtor
@Autowired
private RabbitTemplate rabbitTemplate;

public void enviarPedido(Pedido pedido) {
    PedidoMensagemDTO payload = new PedidoMensagemDTO(
        pedido.getId(),
        pedido.getClienteId(),
        pedido.getTotal(),
        LocalDateTime.now()
    );
    rabbitTemplate.convertAndSend("exchange.pedidos", "pedido.criado", payload);
}

// No consumidor
@RabbitListener(queues = "fila.pedidos")
public void consumirPedido(PedidoMensagemDTO payload, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
    try {
        processar(payload);
        channel.basicAck(tag, false); // Confirma processamento
    } catch (Exception e) {
        // Rejeita com requeue false para ir para DLQ
        channel.basicNack(tag, false, false);
    }
}
```

### Validação de payload em mensageria
```java
@EventListener(ApplicationReadyEvent.class)
public void init() {
    // Configura validador customizado para mensagens
    rabbitTemplate.setBeforePublishPostProcessors(
        message -> {
            Object payload = message.getMessageProperties().getHeaders().get("payload");
            if (payload instanceof Validatable) {
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Set<ConstraintViolation<Validatable>> violations = 
                    factory.getValidator().validate((Validatable) payload);
                if (!violations.isEmpty()) {
                    throw new ValidationException("Payload inválido: " + violations);
                }
            }
            return message;
        }
    );
}
```

---

## 8. Logging Estratégico de Payloads

### Log apenas em desenvolvimento (evitar expor dados sensíveis)
```java
@Slf4j
@RestControllerAdvice
public class LoggingInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (log.isDebugEnabled() && request.getMethod().matches("POST|PUT|PATCH")) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] payload = wrapper.getContentAsByteArray();
            if (payload.length > 0) {
                String body = new String(payload, StandardCharsets.UTF_8);
                log.debug("Payload recebido: {}", maskSensitiveFields(body));
            }
        }
        return true;
    }
    
    private String maskSensitiveFields(String json) {
        // Remove campos como senha, token, cartaoCredito, etc.
        return json.replaceAll("\"senha\"\\s*:\\s*\"[^\"]*\"", "\"senha\":\"****\"")
                   .replaceAll("\"token\"\\s*:\\s*\"[^\"]*\"", "\"token\":\"****\"")
                   .replaceAll("\"cartao\"\\s*:\\s*\"[^\"]*\"", "\"cartao\":\"****\"");
    }
}
```

### Log estruturado com MDC (Mapped Diagnostic Context)
```java
@PostMapping
public ResponseEntity criar(@RequestBody @Valid PedidoRequest payload) {
    // Adiciona ID do payload ao log para rastreabilidade
    MDC.put("payloadId", UUID.randomUUID().toString());
    MDC.put("clienteId", payload.clienteId().toString());
    
    log.info("Processando payload de pedido");
    // ... processamento
    
    MDC.clear(); // Limpa no final
    return ResponseEntity.ok().build();
}
```

### Auditoria de payloads (salvar histórico)
```java
@Entity
public class PayloadAudit {
    @Id @GeneratedValue private Long id;
    private String endpoint;
    private String metodo;
    @Column(columnDefinition = "TEXT")
    private String payload;
    private String usuario;
    private Instant timestamp;
}

@Aspect
@Component
public class PayloadAuditAspect {
    @Around("@annotation(com.yourapp.annotation.AuditPayload)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object payload = Arrays.stream(args)
            .filter(arg -> arg.getClass().isAnnotationPresent(RequestBody.class) || 
                          arg.getClass().getPackageName().contains("dto"))
            .findFirst()
            .orElse(null);
            
        if (payload != null) {
            String json = new ObjectMapper().writeValueAsString(payload);
            saveAudit(json);
        }
        return joinPoint.proceed();
    }
}
```

---

## 9. Payload em Multipart/File Upload

### Estrutura para upload com dados JSON + Arquivo
```java
@PostMapping(value = "/produtos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<ProdutoResponse> criarProduto(
    @RequestPart("produto") @Valid ProdutoRequestDTO payload,  // Dados JSON
    @RequestPart(value = "imagem", required = false) MultipartFile imagem,  // Arquivo
    @RequestPart(value = "documentos", required = false) List<MultipartFile> documentos // Múltiplos
) {
    // payload contém os dados estruturados
    // imagem e documentos são os binários
    return ResponseEntity.ok(produtoService.salvar(payload, imagem, documentos));
}
```

### Validação de tamanho para Multipart
```yaml
spring:
  servlet:
    multipart:
      enabled: true
      max-file-size: 10MB
      max-request-size: 20MB
      file-size-threshold: 2KB # Grava em disco após 2KB
      location: /tmp/uploads
```

### DTO para dados em Multipart
```java
public record ProdutoRequestDTO(
    @NotBlank String nome,
    @NotNull @Positive BigDecimal preco,
    @Pattern(regexp = "[A-Z]{2}") String categoria,  // Ex: EL, ELETRONICOS
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataLancamento,
    @NotNull Boolean disponivel
) {}
```

---

## 10. Testes e Documentação

### Testes de payload com MockMvc
```java
@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser
    void deveCriarProdutoComPayloadValido() throws Exception {
        ProdutoRequestDTO payload = new ProdutoRequestDTO(
            "PROD-001", "Smartphone", new BigDecimal("1999.99")
        );
        
        mockMvc.perform(post("/api/produtos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(payload)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.nome").value("Smartphone"));
    }
    
    @Test
    void deveRetornarErroQuandoPayloadInvalido() throws Exception {
        ProdutoRequestDTO payload = new ProdutoRequestDTO("", null, null);
        
        mockMvc.perform(post("/api/produtos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(payload)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.codigo").value("ERRO_VALIDACAO"))
            .andExpect(jsonPath("$.detalhes").isArray());
    }
}
```

### Documentação com OpenAPI (Swagger)
```java
@Schema(description = "Payload para criação de produto")
public record ProdutoRequestDTO(
    @Schema(description = "Código único do produto", example = "PROD-001", required = true)
    @NotBlank String sku,
    
    @Schema(description = "Nome do produto", example = "Smartphone Galaxy S21", required = true)
    @NotBlank String nome,
    
    @Schema(description = "Preço em reais (R$)", example = "1999.99", minimum = "0.01")
    @Positive BigDecimal preco,
    
    @Schema(description = "Categoria do produto", example = "ELETRONICOS", 
            allowableValues = {"ELETRONICOS", "ROUPAS", "ALIMENTOS"})
    @Pattern(regexp = "ELETRONICOS|ROUPAS|ALIMENTOS")
    String categoria
) {}

// No controller
@Operation(summary = "Cria um novo produto", 
           requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
               content = @Content(examples = @ExampleObject(
                   value = "{\"sku\":\"PROD-001\",\"nome\":\"Notebook\",\"preco\":3500.00}"
               ))
           ))
@PostMapping
public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequestDTO payload) { ... }
```

### Testes de contrato com Spring Cloud Contract
```groovy
Contract.make {
    description "Deve criar um produto com payload válido"
    request {
        method 'POST'
        url '/api/produtos'
        headers {
            contentType 'application/json'
        }
        body '''
            {
                "sku": "PROD-001",
                "nome": "Produto Teste",
                "preco": 99.99
            }
        '''
    }
    response {
        status 200
        body '''
            {
                "id": 1,
                "sku": "PROD-001",
                "nome": "Produto Teste",
                "preco": 99.99
            }
        '''
    }
}
```

---

## 11. Anti-Patterns a Evitar

### ❌ 1. Expor IDs internos no payload de criação
```java
public record PedidoRequest(
    Long id,  // ❌ Cliente não deve enviar ID na criação
    Long clienteId
) {}
```
**Solução:** Use `@Null(groups = OnCreate.class)` ou remova o campo.

---

### ❌ 2. Ignorar validações de negócio
```java
@PostMapping
public ResponseEntity criar(@RequestBody PedidoRequest payload) {
    // ❌ Não validou se cliente existe, se estoque é suficiente, etc.
    pedidoService.salvar(payload);
}
```
**Solução:** Valide no service com exceptions claras.

---

### ❌ 3. Misturar responsabilidades no DTO
```java
public class UsuarioDTO {
    private String nome;
    private String senha;
    private String confirmarSenha; // ❌ Validação no DTO, não lógica de negócio
    
    public boolean senhasConferem() { // ❌ Lógica no DTO
        return senha.equals(confirmarSenha);
    }
}
```
**Solução:** Use validação customizada com `@PasswordMatch` ou faça no service.

---

### ❌ 4. Serializar entidades com relacionamentos LAZY
```java
@Entity
public class Pedido {
    @OneToMany(fetch = FetchType.LAZY)
    private List<Item> itens; // ❌ Ao serializar, causa LazyInitializationException
}
```
**Solução:** Use DTOs e mapeie explicitamente os campos necessários.

---

### ❌ 5. Não tratar payloads nulos
```java
@PostMapping
public ResponseEntity criar(@RequestBody PedidoRequest payload) {
    // ❌ Se payload for null, NullPointerException
    String nome = payload.nome();
}
```
**Solução:** Use `@RequestBody(required = true)` (default) ou valide no handler.

---

### ❌ 6. Logar payloads sensíveis sem mascaramento
```java
log.info("Payload recebido: {}", payload); // ❌ Expor senhas, cartões de crédito
```
**Solução:** Use `@JsonIgnore` ou mascaramento antes de logar.

---

## Resumo Final

| Boa Prática | Benefício |
|-------------|-----------|
| DTOs em vez de Entidades | Desacoplamento, segurança, evolução |
| Validação robusta (`@Valid` + custom) | Dados consistentes desde a entrada |
| Versionamento de API | Evolução sem quebrar clientes |
| Limites de tamanho | Proteção contra DoS |
| Serialização eficiente | Performance, imutabilidade |
| Tratamento global de erros | UX consistente e clara |
| Logging estratégico | Debugging sem expor dados sensíveis |
| Testes com MockMvc | Qualidade e prevenção de regressões |
| Documentação OpenAPI | API auto-descritiva |

---

## Referências
- [Spring Documentation - RequestBody](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/requestbody.html)
- [Bean Validation 2.0](https://beanvalidation.org/)
- [Jackson Best Practices](https://github.com/FasterXML/jackson-docs)
- [OWASP - Input Validation Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html)
- [Spring Boot - Multipart File Upload](https://docs.spring.io/spring-boot/reference/io/part-file.html)

---

**Última atualização:** 2026-08-14  
**Versão do Spring Boot:** 3.x  
**Java:** 17+
```

---

Posso complementar com alguma seção específica que você queira aprofundar ainda mais? (Ex: performance com grandes payloads, integração com GraphQL, ou payloads assíncronos com WebFlux)