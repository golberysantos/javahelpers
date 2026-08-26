# Mockito - Referência Completa

Esta página é uma referência detalhada sobre Mockito, uma das bibliotecas de mocking mais populares para testes unitários em Java. O conteúdo abaixo foi escrito para ser consultado por desenvolvedores que desejam entender conceitos, ver exemplos práticos, conhecer boas práticas e esclarecer as dúvidas mais frequentes.

Sumário
- O que é Mockito?
- Conceitos-chave
  - Mock, Stub, Spy
  - When/Then, doReturn/doThrow
  - ArgumentCaptor e Matchers
  - Verificação (verify)
- Exemplos práticos
  - Configuração básica com JUnit 5
  - Uso de mocks e stubs
  - Spies e partial mocks
  - Capturando argumentos
  - Verificando ordem e número de chamadas
- Boas práticas
- Armadilhas comuns e como evitá-las
- Principais dúvidas (FAQ)


O que é Mockito?
-----------------

Mockito é uma biblioteca Java open-source para criação de objetos mock (falsos) em testes unitários. Ela permite simular o comportamento de dependências do código sob teste, controlar retornos, verificar interações e capturar argumentos — tudo isso sem precisar instanciar implementações reais ou dependências externas (bancos, serviços HTTP, etc.).

Mockito é focada em facilitar os testes de unidade, mantendo-os rápidos, independentes e determinísticos.


Conceitos-chave
---------------

- Mock: um objeto falso criado por Mockito que substitui uma dependência. Você configura o comportamento (quando uma chamada ocorrer, retorne X) e verifica interações (quantas vezes um método foi chamado, com que argumentos, etc.).
- Stub: geralmente se refere à configuração de retornos de um mock (por exemplo, when(mock.method()).thenReturn(value)). Em Mockito, costumamos não diferenciar estritamente mock de stub; mock é o objeto criado e stub é a configuração do comportamento.
- Spy: uma forma de criar um wrapper parcial ao redor de um objeto real. Um spy chama os métodos reais por padrão, mas você pode sobrescrever comportamentos específicos. Útil para testar classes que você quer usar parcialmente reais e parcialmente falsificadas.

- when/then: API clássica para configurar comportamento. Ex.: when(service.call()).thenReturn(result).
- doReturn/doThrow/doAnswer: alternativa para configurar métodos void ou quando usar when() causaria problemas (por exemplo, em spies ou métodos finais). Ex.: doReturn(value).when(spy).method();

- ArgumentCaptor: classe para capturar os argumentos que foram passados a um mock, permitindo fazer assertivas nos valores usados durante a chamada.

- Matchers: utilitários para combinar argumentos de forma flexível (any(), eq(), argThat(), etc.). Quando se usa matchers, todos os argumentos em uma chamada devem usar matchers ou serem valores literais compatíveis com eq().

- verify: método para verificar interações com mocks, p.ex.: verify(mock, times(2)).save(item);


Dependências recomendadas
-------------------------

Antes de executar os exemplos abaixo, é uma boa prática definir uma propriedade de versão e incluir `mockito-core`. Se você precisa mockar métodos estáticos ou finais em tempo de teste, adicione também `mockito-inline`:

```xml
<properties>
  <mockito.version>5.2.0</mockito.version> <!-- ajuste para a versão estável mais recente -->
</properties>

<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>${mockito.version}</version>
  <scope>test</scope>
</dependency>

<!-- Opcional: necessário para mockar métodos estáticos/inline -->
<dependency>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-inline</artifactId>
  <version>${mockito.version}</version>
  <scope>test</scope>
</dependency>
```

Exemplos práticos
------------------

Exemplo básico com JUnit 5:

```java
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ServiceTest {
	@Test
	void exemploMock() {
		// cria mock
		Dependency dep = mock(Dependency.class);

		// configura stub
		when(dep.calculate(2)).thenReturn(4);

		// injeta mock no SUT
		Service sut = new Service(dep);

		// executa
		int result = sut.doubleCalculate(2);

		// verifica retorno
		assertEquals(8, result);

		// verifica interação
		verify(dep, times(1)).calculate(2);
	}
}
```

Notas:
- Use `when(...).thenReturn(...)` para métodos não-void.
- Para métodos void ou quando trabalhar com spies, prefira `doReturn(...).when(...);` ou `doThrow(...)`.

Spy (partial mock):

```java
List<String> list = new ArrayList<>();
List<String> spyList = spy(list);

spyList.add("one");
spyList.add("two");

// sobrescreve comportamento do size()
doReturn(100).when(spyList).size();

assertEquals(100, spyList.size());
```

ArgumentCaptor:

```java
ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
verify(repo).save(captor.capture());
User saved = captor.getValue();
assertEquals("alice", saved.getName());
```

Matchers:

```java
when(service.process(anyString(), eq(5))).thenReturn("ok");
```

Verificando ordem e número de chamadas:

```java
InOrder inOrder = inOrder(mock1, mock2);
inOrder.verify(mock1).first();
inOrder.verify(mock2).second();

verify(mock, never()).method();
verify(mock, atLeastOnce()).method();
verify(mock, times(3)).method();
```


Boas práticas
-------------

- Prefira mocks para dependências externas (bancos, serviços remotos). Teste a lógica do SUT (System Under Test) isoladamente.
- Não abuse de spies: eles podem esconder problemas de design. Prefira usar instâncias reais pequenas e imutáveis quando fizer sentido.
- Evite over-mocking (mockar tudo) — isso torna testes frágeis e acoplados à implementação. Mock apenas dependências que são custosas, não-determinísticas ou externas.
- Use ArgumentCaptor para validar dados críticos que foram enviados a dependências.
- Use matchers com consistência: se usar um matcher para um argumento, use matchers para todos ou combine com eq() para valores literais.
- Mantenha os testes simples e legíveis: separação clara entre arrange-act-assert.


Armadilhas comuns e como evitá-las
---------------------------------

- Problema: usar `when(spy.method())` em um spy que executa lógica real com efeitos colaterais. Solução: prefira `doReturn(...).when(spy).method()`.
- Problema: misturar matchers e valores literais. Solução: ou use matchers para todos os argumentos ou use `eq()` para valores literais.
- Problema: verificar comportamento de implementação em vez de contrato. Solução: foque em verificar efeitos observáveis e resultados, não chamadas internas desnecessárias.
- Problema: testes que dependem da ordem de verificação sem necessidade. Solução: use `InOrder` somente quando a ordem for parte do contrato.


Principais dúvidas (FAQ)
-----------------------

1) Mockito substitui frameworks de integração e testes end-to-end?

R: Não. Mockito é específico para testes unitários e simula dependências. Para testes de integração ou e2e você deve usar instâncias reais de componentes, bancos de dados em memória, ou ferramentas de integração (Testcontainers, Spring Test, etc.).

2) Como mockar métodos estáticos ou final?

R: Versões antigas do Mockito não suportavam métodos estáticos diretamente; isso mudou e agora, a partir do Mockito 3.4+ (com extensão mockito-inline) e mais estável nas versões 4/5, é possível mockar métodos estáticos usando o recurso inline (mockito-inline) ou com frameworks auxiliares (PowerMock anteriormente). Para mockar finais/estáticos, adicione `mockito-inline` ao classpath de testes.

Exemplo de mock static (API básica):

```java
try (MockedStatic<Util> utilities = mockStatic(Util.class)) {
	utilities.when(() -> Util.now()).thenReturn(fixedTime);
	// ... teste ...
}
```

3) Posso usar Mockito com JUnit 5?

R: Sim. Use a integração `@ExtendWith(MockitoExtension.class)` e as anotações `@Mock`, `@InjectMocks` para gerenciar mocks automaticamente.

Exemplo com JUnit 5:

```java
@ExtendWith(MockitoExtension.class)
class MyTest {
  @Mock
  Dependency dep;

  @InjectMocks
  Service sut;

  @Test
  void teste() {
	when(dep.x()).thenReturn(...);
	// ...
  }
}
```

4) O que `@InjectMocks` faz?

R: Cria a instância do SUT e injeta automaticamente os mocks anotados com `@Mock` por injeção de construtor, setter ou campos (por reflexão). Útil para reduzir boilerplate, mas cuidado para testes que exigem controle explícito de dependências.

5) Como lidar com métodos que lançam exceções (void)?

R: Use `doThrow(new RuntimeException()).when(mock).voidMethod();` para simular exceções em métodos void.

6) Por que meu stub não está sendo chamado?

R: Possíveis causas:
  - O objeto usado na chamada não é o mesmo mock que você configurou (verifique instâncias e injeção).
  - Os argumentos usados na chamada não correspondem ao matcher ou valores esperados (considere usar matchers como any()).
  - Você está usando spy e o método real lança exceção/tem comportamento inesperado; use doReturn se necessário.

7) Mockito é thread-safe?

R: Instâncias de mocks não são intrinsecamente thread-safe para manipulação concorrente no teste. Em geral, testes unitários devem executar em thread única. Se você precisa testar comportamento concorrente, considere ferramentas e cuidado extra.


Referências e leitura adicional
------------------------------

- Documentação oficial: https://site.mockito.org/
- Guia de melhores práticas e padrões de teste: procurar por "Mockito best practices".
- Artigos sobre teste de unidade com JUnit 5 e Mockito (integração com Spring Boot quando aplicável).


Última nota
-----------

Este documento foi criado para ser um guia prático e de referência. Se quiser, posso:
- adicionar exemplos mais avançados (integration with Spring, Testcontainers, async testing),
- traduzir para inglês, ou
- criar exemplos executáveis em um pequeno projeto de amostra.


Mockito com Spring Boot
-----------------------

Integração com Spring Boot é um caso de uso muito comum. Spring Test traz utilitários próprios e anotações para facilitar testes de camadas (controller, service, repository) em conjunto com Mockito.

Conceitos importantes:
- `@MockBean`: cria um mock (Mockito) e o registra no ApplicationContext do Spring, substituindo a bean original. Use em testes que carregam contexto do Spring (ex.: `@WebMvcTest`, `@SpringBootTest`).
- `@SpyBean`: similar ao `@MockBean`, mas cria um spy e o registra no contexto; útil quando se quer um bean real parcialmente espiado.
- `@Mock` / `@InjectMocks` + `MockitoExtension`: ideal para testes puros/unitários que não inicializam o contexto do Spring.

Regra prática: ou você testa com contexto Spring e usa `@MockBean`/`@SpyBean`, ou testa puramente com Mockito e usa `@Mock` + `@ExtendWith(MockitoExtension.class)`; não tente misturar `MockitoExtension` com `SpringExtension` no mesmo teste.

Exemplo — teste de Controller com `@WebMvcTest` e `@MockBean`:

```java
// Controller
@RestController
class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> get(@PathVariable String id) {
        return ResponseEntity.ok(userService.find(id));
    }
}

// Test
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService; // substitui o bean no contexto

    @Test
    void shouldReturnUser() throws Exception {
        when(userService.find("1")).thenReturn(new UserDto("1", "Alice"));

        mockMvc.perform(get("/users/1").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Alice"));
    }
}
```

Exemplo — teste de integração parcial com `@SpringBootTest` substituindo um bean com `@MockBean`:

```java
@SpringBootTest
@AutoConfigureMockMvc
class AppIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ExternalClient client; // o client real é substituído pelo mock

    @Test
    void flowWithMockedClient() throws Exception {
        when(client.call()).thenReturn("stubbed");

        mockMvc.perform(get("/do-call"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("stubbed")));
    }
}
```

Exemplo — teste unitário de Service sem Spring (mais rápido):

```java
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    @Mock
    UserRepository repo;

    @InjectMocks
    UserService service; // instância criada e dependência injetada

    @Test
    void unitTest() {
        when(repo.findById("1")).thenReturn(Optional.of(new User("1", "Bob")));
        var dto = service.find("1");
        assertEquals("Bob", dto.getName());
    }
}
```

Notas e cuidados práticos:
- `@MockBean` registra o mock no contexto do Spring e garante que todos os beans que dependem da bean original receberão o mock.
- Prefira `@WebMvcTest` para testar controllers isoladamente (carrega fatias do contexto necessárias ao MVC) e `@DataJpaTest` para repositórios, etc.
- Use `@SpyBean` com cuidado: spies registrados no contexto podem chamar lógica real que dependa de outros beans do contexto.
- Evite carregar `@SpringBootTest` desnecessariamente para testes rápidos — eles são mais lentos. Use testes unitários com `MockitoExtension` sempre que possível.

Exemplo de `@SpyBean`:

```java
@SpringBootTest
class SomeIntegrationTest {
    @SpyBean
    MyService myService; // spy da bean real

    @Test
    void partialSpy() {
        doReturn("forced").when(myService).externalCall();
        // o restante do comportamento da bean permanece real
    }
}
```

## Estrutura padrão

Sim, `src/test/java` é o padrão — mas vale entender **por que** é padrão e não apenas aceitar como regra arbitrária.

Esse layout vem do **Maven Standard Directory Layout**, adotado também pelo Gradle (com pequenas variações de configuração). Não é uma convenção do Mockito em si — o Mockito é só uma biblioteca de mocking que roda dentro dos testes; quem define *onde* os testes ficam é a ferramenta de build (Maven/Gradle) através do seu ciclo de vida.

```
meu-projeto/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/empresa/servico/PedidoService.java
│   └── test/
│       └── java/
│           └── com/empresa/servico/PedidoServiceTest.java
```

### Quem é responsável por cada parte do mecanismo

| Responsável | Função |
|---|---|
| **Maven (`maven-compiler-plugin`)** | Compila `src/main/java` para `target/classes` e `src/test/java` para `target/test-classes` — em fases separadas do build lifecycle (`compile` vs `test-compile`) |
| **Surefire Plugin (Maven)** ou **Gradle Test task** | Varre `target/test-classes`, identifica classes que batem com o padrão `*Test.java`, `Test*.java` ou `*Tests.java`, e as executa na fase `test` |
| **JUnit (Jupiter/4)** | Fornece o runner/engine que de fato instancia a classe de teste e invoca os métodos anotados com `@Test` |
| **Mockito** | Atua *dentro* desse ciclo — via `@ExtendWith(MockitoExtension.class)` (JUnit 5) ou `MockitoJUnitRunner` (JUnit 4) — para inicializar os `@Mock`, `@InjectMocks` e validar stubbings *antes* de cada método de teste rodar |

### Onde a "regra absoluta" quebra

Isso não é obrigatório — é convenção pra funcionar com **zero configuração**. Você pode reconfigurar via `pom.xml` (`<testSourceDirectory>`) ou `build.gradle` (`sourceSets.test.java.srcDirs`), e isso é comum em cenários como:

- **Monorepos** com múltiplos módulos que compartilham uma pasta de testes de integração separada (`src/it/java`, convenção do `maven-failsafe-plugin` para testes de integração vs. unitários)
- **Testes de contrato/BDD** organizados em `src/test/resources` para os `.feature` files (Cucumber) enquanto os step definitions ficam em `src/test/java`
- Projetos legados migrados de Ant, onde a estrutura original era preservada por custo de migração

### Um ponto de atenção real (não cosmético)

O pacote da classe de teste **deve espelhar o pacote da classe testada** (`com.empresa.servico.PedidoServiceTest` testando `com.empresa.servico.PedidoService`) — isso não é estética, é o que permite acesso a membros `package-private`/`protected` sem reflection, algo relevante quando você quer testar um método sem expor `public` desnecessariamente na API.

Quer que eu detalhe a diferença de convenção entre testes unitários (Surefire) e testes de integração (Failsafe), já que isso costuma gerar confusão sobre onde colocar testes que usam `@SpringBootTest` com Mockito?

## Surefire vs. Failsafe: duas fases, duas responsabilidades

A confusão é legítima porque o Maven não separa isso por *pasta* por padrão — separa por **naming convention** e **plugin binding**. Vamos por partes.

### O mecanismo por trás

| Responsável | Fase do lifecycle | O que executa |
|---|---|---|
| **maven-surefire-plugin** | `test` | Testes unitários — roda **antes** do `package` |
| **maven-failsafe-plugin** | `integration-test` + `verify` | Testes de integração — roda **depois** do artefato ser empacotado |

Essa diferença de fase é o ponto central: o Surefire roda antes de gerar o `.jar`/`.war`, então ele não pode (e não deve) depender de um contexto de aplicação completo subindo. O Failsafe roda **depois** do pacote pronto, e tem uma característica crítica que o Surefire não tem: ele executa a fase `verify` mesmo que os testes falhem na fase `integration-test`, permitindo que um goal `post-integration-test` (ex: derrubar um container Docker, limpar um banco) rode antes do build efetivamente quebrar.

### Convenção de nomes (é isso que decide quem roda o quê)

```
src/test/java/
├── com/empresa/servico/
│   ├── PedidoServiceTest.java          → Surefire (unitário)
│   ├── PedidoServiceUnitTest.java      → Surefire (se configurado)
│   └── PedidoServiceIT.java            → Failsafe (integração)
```

- **Surefire**: por padrão pega `**/Test*.java`, `**/*Test.java`, `**/*Tests.java`, `**/*TestCase.java`
- **Failsafe**: por padrão pega `**/IT*.java`, `**/*IT.java`, `**/*ITCase.java`

Ambos vivem em `src/test/java` — **não há pasta separada por padrão**. Isso já derruba uma crença comum de que "teste de integração vai em outra pasta". Só vai se você reconfigurar `<testSourceDirectory>` manualmente (comum em monorepos, mas não é o default do Maven).

### Onde entra `@SpringBootTest` + Mockito

Aqui mora a armadilha real, não cosmética: **`@SpringBootTest` + `@MockBean` não é a mesma coisa que `@ExtendWith(MockitoExtension.class)` + `@Mock`**, e confundir os dois é a causa mais comum de suites lentas.

| Anotação | O que faz | Custo |
|---|---|---|
| `@ExtendWith(MockitoExtension.class)` + `@Mock`/`@InjectMocks` | Cria mocks puros via bytecode manipulation (Mockito), **sem** subir o Spring Context | Milissegundos — é teste unitário de verdade |
| `@SpringBootTest` + `@MockBean` | Sobe o `ApplicationContext` inteiro (ou fatiado, com `@WebMvcTest`/`@DataJpaTest`), e **substitui** o bean real pelo mock no container do Spring | Segundos por classe — o Spring cacheia o contexto entre classes com a mesma config, mas qualquer `@MockBean` diferente invalida esse cache e força reload |

**Consequência prática de arquitetura de suíte:** se você espalha `@SpringBootTest` com `@MockBean`s diferentes em cada classe, você paga o custo de subir o contexto do Spring dezenas de vezes, mesmo que cada teste isoladamente pareça "rápido". Isso é responsabilidade do **Spring TestContext Framework** (especificamente o `DefaultCacheAwareContextLoaderDelegate`), que mantém um cache de contextos por assinatura — e `@MockBean` entra nessa assinatura.

### Recomendação com ressalva (não é regra absoluta)

Uma prática comum e defensável:
- `PedidoServiceTest.java` (Surefire) → `MockitoExtension`, mocka `PedidoRepository`, testa lógica de negócio isolada
- `PedidoControllerIT.java` (Failsafe) → `@SpringBootTest(webEnvironment = RANDOM_PORT)`, testa a integração real HTTP → Controller → Service, com `@MockBean` só na borda externa (ex: client de API externa), não no repository

Mas isso **não é lei**: em times menores, ou com poucas dependências externas caras, subir o `@SpringBootTest` completo já na fase `test` (Surefire) pode ser aceitável — o overhead do Spring hoje (com JVM moderna e reuse de contexto) é menor do que era há alguns anos. A decisão de separar em Failsafe compensa quando há custo real de infraestrutura (containers Testcontainers, banco real, mensageria) que você quer isolar do `mvn test` rápido do dia a dia.

Quer que eu monte um exemplo concreto com Testcontainers + Failsafe + Mockito, mostrando como isolar um teste de repositório JPA de um teste de service mockado?

## Exemplo: configuração do Failsafe no `pom.xml` (e dicas)

Abaixo está um exemplo mínimo de configuração no `pom.xml` para separar testes unitários (Surefire) e testes de integração (Failsafe). Também incluo dependências de exemplo para Testcontainers (útil em testes de integração que precisam de um banco real em container).

```xml
<properties>
  <maven.surefire.version>3.0.0-M7</maven.surefire.version>
  <maven.failsafe.version>3.1.2</maven.failsafe.version>
  <testcontainers.version>1.17.6</testcontainers.version>
</properties>

<dependencies>
  <!-- Testcontainers (exemplo: PostgreSQL) -->
  <dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>${testcontainers.version}</version>
    <scope>test</scope>
  </dependency>
  <!-- Mockito e JUnit já foram mostrados acima -->
</dependencies>

<build>
  <plugins>
    <!-- Surefire: executa testes unitários durante a fase 'test' -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>${maven.surefire.version}</version>
      <configuration>
        <includes>
          <include>**/*Test.java</include>
        </includes>
      </configuration>
    </plugin>

    <!-- Failsafe: executa testes de integração nas fases 'integration-test' e 'verify' -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <version>${maven.failsafe.version}</version>
      <configuration>
        <includes>
          <include>**/*IT.java</include>
        </includes>
      </configuration>
      <executions>
        <execution>
          <goals>
            <goal>integration-test</goal>
            <goal>verify</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

Dicas rápidas:
- Nomeie seus testes de integração usando o sufixo `IT` (ex: `PedidoControllerIT`) para que o Failsafe os execute; teste unitário continue com `*Test`.
- Configure Testcontainers dentro dos testes de integração para criar dependências (banco, Kafka, etc.) e faça o teardown automaticamente.
- Use perfis Maven (ex: `-P integration-tests`) se quiser controlar quando executar os ITs no pipeline.

Links úteis e referências
- Maven Surefire Plugin: https://maven.apache.org/surefire/maven-surefire-plugin/
- Maven Failsafe Plugin: https://maven.apache.org/surefire/maven-failsafe-plugin/
- Testcontainers: https://www.testcontainers.org/
- Spring Boot Test (documentação de testes): https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing