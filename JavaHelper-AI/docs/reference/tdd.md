# TDD (Test-Driven Development)
> Documento elaborado sob a ótica de um Engenheiro de QA (testes em Java) com atuação também em automação de fluxos (N8N). O objetivo é explicar o mecanismo de forma cirúrgica: quem faz o quê, em que ordem, e sob quais condições cada afirmação é válida — sem tratar boas práticas contextuais como regras universais.

---

## 1. O que é TDD, de fato

TDD (Test-Driven Development, ou Desenvolvimento Orientado a Testes) é uma **técnica de desenvolvimento de software**, não uma técnica de teste em si. Isso é uma distinção importante: o produto final do TDD não é "ter testes" — é **usar o teste como ferramenta de design** para guiar decisões de código antes que o código exista.

Foi popularizado por **Kent Beck**, no contexto do movimento Extreme Programming (XP), no final dos anos 1990. A premissa central é simples de enunciar e difícil de sustentar na prática:

> "Nunca escreva uma linha de código de produção sem antes ter um teste automatizado que falhe por causa da ausência dela."

O responsável por essa disciplina é sempre o **desenvolvedor/QA que está escrevendo o código**, não uma ferramenta. TDD não é automatizável por si só — é um hábito de trabalho.

---

## 2. O ciclo Red-Green-Refactor

Esse é o mecanismo central do TDD. Cada fase tem um responsável e um critério de saída bem definidos:

### 🔴 RED — Escrever um teste que falha
- **Responsável:** o desenvolvedor.
- **Ação:** escrever um teste automatizado (ex.: um método anotado com `@Test` no JUnit) que descreve um comportamento que ainda não existe no sistema.
- **Critério de saída:** o teste deve **falhar** — e falhar pelo motivo certo (classe/método inexistente, asserção não satisfeita), não por erro de compilação acidental ou configuração quebrada.
- **Responsável pela verificação de falha:** o framework de execução (JUnit 5, TestNG) e o runner (Maven Surefire, Gradle Test), que reportam o status vermelho.

### 🟢 GREEN — Fazer o teste passar com o mínimo de código
- **Responsável:** o desenvolvedor.
- **Ação:** escrever a implementação **mais simples possível** que satisfaça o teste. Não é o momento de generalizar, otimizar ou antecipar requisitos futuros.
- **Critério de saída:** todos os testes (o novo e os anteriores) passam.
- **Responsável pela verificação:** novamente o executor de testes (JUnit + build tool), que reporta o status verde.

### ♻️ REFACTOR — Melhorar o código sem alterar comportamento
- **Responsável:** o desenvolvedor.
- **Ação:** eliminar duplicação, melhorar nomes, extrair métodos/classes, aplicar padrões — mantendo a bateria de testes verde o tempo todo.
- **Critério de saída:** o design melhora e os testes continuam passando, sem nenhum teste novo ser adicionado nesta fase.
- **Responsável pela garantia de segurança:** a suíte de testes já existente atua como **rede de proteção** (safety net) contra regressões.

Esse ciclo se repete em intervalos curtos — idealmente minutos, não horas. A curta duração do ciclo é o que diferencia TDD de "escrever testes depois, em lote".

---

## 3. TDD não é "escrever testes primeiro" apenas

Uma armadilha comum é reduzir TDD a "inverter a ordem": escrever o teste antes do código de produção. Isso é necessário, mas não suficiente. O valor real do TDD está em três efeitos colaterais do processo:

1. **Design orientado por uso (API primeiro):** ao escrever o teste antes, você é forçado a pensar em como a classe/método será *consumido*, não em como será *implementado*. Isso tende a produzir interfaces mais limpas — mas isso depende da disciplina de quem escreve o teste; TDD não garante bom design por si só.
2. **Escopo mínimo por construção:** como você só escreve código para fazer um teste passar, é estruturalmente mais difícil (embora não impossível) fazer over-engineering.
3. **Cobertura como subproduto, não como meta:** a cobertura de testes surge naturalmente do processo, em vez de ser perseguida depois via métricas de ferramenta (ex.: JaCoCo). Vale a ressalva: cobertura alta não implica testes de qualidade — é possível ter 100% de cobertura de linha com asserções fracas.

---

## 4. Classicista (Detroit/Chicago) vs. Mockista (London)

Essa é uma divergência real e não resolvida na comunidade — tratá-la como "certo vs. errado" seria um erro de generalização.

| Aspecto | Escola Clássica (Detroit) | Escola Mockista (London) |
|---|---|---|
| Foco do teste | Estado final do objeto | Interações entre objetos (colaboradores) |
| Uso de dublês (mocks/stubs) | Mínimo — prioriza objetos reais | Extensivo — isola cada unidade de suas dependências |
| Direção do design | De baixo para cima (bottom-up) | De cima para baixo (top-down), guiado por contratos |
| Ferramenta típica em Java | JUnit + AssertJ | JUnit + Mockito + AssertJ |
| Risco característico | Testes mais "de integração" sem perceber | Testes acoplados à implementação (mocks demais quebram a cada refactor) |

**Não existe consenso de que uma escola seja superior.** A escolha costuma depender do tipo de camada testada: lógica de domínio pura tende a se beneficiar do estilo clássico; orquestração de serviços com múltiplas dependências externas costuma justificar o estilo mockista.

---

## 5. Exemplo prático em Java (JUnit 5 + AssertJ + Mockito)

Cenário: uma classe `DescontoService` que aplica desconto a um pedido, delegando a validação de elegibilidade a um `ClienteRepository`.

### RED — teste que ainda não compila/passa
```java
class DescontoServiceTest {

    private final ClienteRepository clienteRepository = mock(ClienteRepository.class);
    private final DescontoService descontoService = new DescontoService(clienteRepository);

    @Test
    void deveAplicar10PorCentoParaClientePremium() {
        // given
        when(clienteRepository.buscarPorId(1L))
            .thenReturn(new Cliente(1L, TipoCliente.PREMIUM));

        // when
        BigDecimal valorComDesconto = descontoService.calcular(1L, new BigDecimal("100.00"));

        // then
        assertThat(valorComDesconto).isEqualByComparingTo("90.00");
    }
}
```
Neste ponto, `DescontoService` nem existe — o teste falha na compilação, o que é considerado "vermelho" no sentido amplo do ciclo.

### GREEN — implementação mínima
```java
public class DescontoService {

    private final ClienteRepository clienteRepository;

    public DescontoService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public BigDecimal calcular(Long clienteId, BigDecimal valor) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId);
        if (cliente.getTipo() == TipoCliente.PREMIUM) {
            return valor.multiply(new BigDecimal("0.90"));
        }
        return valor;
    }
}
```
O teste passa. Ainda não há tratamento de `cliente == null`, nem de outros tipos de desconto — porque nenhum teste exigiu isso ainda. Isso é intencional no TDD: **o código só cresce quando um teste o exige.**

### REFACTOR
Aqui poderíamos extrair a regra `0.90` para uma constante nomeada, ou mover a lógica de percentual para um `enum TipoCliente` com método próprio — desde que o teste continue verde durante toda a mudança.

**Responsáveis no fluxo:**
- **JUnit 5** — motor de execução, ciclo de vida dos testes (`@BeforeEach`, `@Test`), engine de descoberta.
- **Mockito** — criação do dublê `clienteRepository`, controle de comportamento simulado (`when/thenReturn`), e (quando necessário) verificação de interação (`verify`).
- **AssertJ** — biblioteca de asserções fluentes, responsável pela legibilidade e pela mensagem de erro em caso de falha.
- **Maven/Gradle Surefire/Test task** — responsável por rodar a suíte no build e falhar o pipeline caso algum teste quebre.

---

## 6. TDD aplicado a automações N8N

TDD, na sua forma canônica, pressupõe um ciclo rápido de compilação/execução local — algo natural em Java, mas que precisa ser **adaptado** (não copiado) para fluxos visuais como N8N. Não é honesto dizer "faça TDD em N8N" sem qualificar o que isso significa na prática, já que N8N não é uma linguagem de programação tradicional com um test runner nativo.

Formas realistas de aplicar a mentalidade TDD em N8N:

1. **Nodes de código (`Code Node` / `Function Node`):**
   Aqui o TDD clássico se aplica quase sem adaptação. O responsável (desenvolvedor do workflow) pode extrair a lógica JavaScript do Code Node para um módulo isolado, testá-lo com **Jest** ou **Vitest** fora do N8N (RED → GREEN → REFACTOR), e só então colar/importar a lógica validada no node. Isso evita testar lógica de negócio "dentro" da interface visual, onde o feedback é lento.

2. **Contratos de Webhook/Trigger:**
   Antes de montar o fluxo, define-se o *schema* esperado do payload de entrada (ex.: com JSON Schema ou Zod) e um teste automatizado (fora do N8N, via chamada HTTP simulada) que valida esse contrato. O fluxo N8N é então construído para satisfazer esse contrato — inversão de ordem que é o espírito do TDD, mesmo sem um "test runner" dentro da ferramenta.

3. **Testes de integração do workflow completo:**
   Como o N8N não roda testes unitários nativamente, a validação do fluxo como um todo tende a ser feita via:
   - Ambiente de staging com **execuções manuais controladas** (pin data / dados fixos por node), comparando a saída esperada;
   - Chamadas HTTP externas (ex.: com **RestAssured**, se o QA já trabalha em Java) que disparam o Webhook do N8N e validam a resposta final — funcionando como um teste de "caixa preta" do workflow.
   - **Responsável por essa camada:** normalmente o QA, não o desenvolvedor do fluxo, já que é uma validação de contrato externo.

4. **Limite real da abordagem:** não existe, hoje, um framework de TDD nativo e amplamente adotado para os nós visuais do N8N (diferente de testar uma classe Java). Afirmar o contrário seria impreciso. O que existe é uma **adaptação disciplinada**: isolar lógica testável em código (Function Nodes) e tratar o restante do fluxo como integração, testada por fora.

---

## 7. Benefícios (com as devidas ressalvas)

| Benefício frequentemente citado | Ressalva necessária |
|---|---|
| Reduz bugs de regressão | Depende da qualidade das asserções, não só da existência dos testes |
| Melhora o design (baixo acoplamento) | Depende da disciplina de quem escreve; TDD mal aplicado também produz código ruim |
| Documentação viva do comportamento | Só funciona se os nomes dos testes forem descritivos — não é automático |
| Aumenta a velocidade a longo prazo | No curto prazo, quase sempre **reduz** velocidade — é um investimento, não um ganho imediato |
| Cobertura alta | Cobertura é métrica de execução, não de qualidade de asserção; pode ser enganosa |

Não é razoável apresentar TDD como solução universal. Em protótipos descartáveis, exploração de UI, ou código com requisitos extremamente instáveis, o custo do ciclo Red-Green-Refactor pode superar o benefício — esse é um julgamento de contexto, não uma exceção "proibida".

---

## 8. Armadilhas comuns observadas em times reais

- **Testar implementação em vez de comportamento:** mocks excessivos fazem o teste quebrar a cada refactor, mesmo sem mudança de comportamento — sintoma clássico de "over-mocking" na escola London.
- **Pular a fase GREEN mínima:** implementar mais do que o teste exige "porque já sei que vai precisar" quebra o princípio de escopo mínimo e tende a gerar código não testado.
- **Pular REFACTOR:** acumular débito técnico porque "os testes já estão verdes" ignora que o ciclo tem três fases, não duas.
- **Confundir TDD com BDD:** BDD (Behavior-Driven Development, com ferramentas como Cucumber) foca em linguagem ubíqua e colaboração com negócio; TDD é uma técnica de design de código. Podem coexistir, mas não são sinônimos.
- **Achar que TDD substitui testes de integração/E2E:** TDD atua principalmente no nível de unidade. Fluxos completos (como um workflow N8N ponta a ponta) ainda exigem uma camada de teste de integração separada.

---

## 9. Perguntas Frequentes (FAQ)

**1. TDD garante ausência de bugs?**
Não. Garante que o comportamento especificado pelos testes está correto no momento da execução. Bugs em requisitos não cobertos por nenhum teste continuam possíveis.

**2. Preciso escrever teste para getters/setters simples?**
Na maioria dos casos, não agrega valor de design nem de proteção contra regressão — mas se o getter contém lógica (ex.: cálculo derivado), a resposta muda. Julgamento de contexto, não regra fixa.

**3. TDD funciona bem com testes de UI/E2E?**
É mais difícil e menos comum, porque o ciclo Red-Green-Refactor pressupõe feedback rápido, e testes de UI (Selenium, Playwright) tendem a ser mais lentos e frágeis. Muitos times aplicam TDD apenas nas camadas de unidade/serviço e tratam E2E como uma malha de segurança separada, escrita depois.

**4. Qual a diferença entre TDD e "Test-First" sem disciplina de ciclo?**
"Test-First" descreve apenas a ordem (teste antes do código). TDD exige o ciclo completo, incluindo o refactor disciplinado com os testes como rede de segurança. É possível fazer test-first sem fazer TDD de fato.

**5. Em Java, JUnit 4 ou JUnit 5 para TDD?**
Tecnicamente ambos suportam o ciclo. JUnit 5 (Jupiter) é a versão atual mantida ativamente, com melhor suporte a testes parametrizados (`@ParameterizedTest`) e extensões (`@ExtendWith`), frequentemente combinado com Mockito via `MockitoExtension`.

**6. TDD e Clean Architecture/Hexagonal se relacionam?**
Sim, indiretamente: escrever o teste antes força pensar na interface do caso de uso antes dos detalhes de infraestrutura, o que tende a favorecer a inversão de dependência — mas TDD não exige nenhuma arquitetura específica.

**7. É possível fazer TDD em N8N da mesma forma que em Java?**
Não da mesma forma. Como detalhado na seção 6, a prática precisa ser adaptada: lógica testável isolada em Code Nodes (testável com Jest/Vitest) e o restante do fluxo validado por testes de integração externos ao N8N.

**8. Quanto tempo um ciclo Red-Green-Refactor deveria durar?**
Não há um número oficial e universal. Na prática de times disciplinados, cada ciclo costuma durar de segundos a poucos minutos. Ciclos de dezenas de minutos geralmente indicam que o passo está grande demais e deveria ser quebrado.

**9. TDD é obrigatório para ser um bom QA/desenvolvedor?**
Não. É uma técnica entre várias (incluindo BDD, testes baseados em propriedades — property-based testing, testes de mutação com PIT). A escolha depende do contexto do time, da criticidade do sistema e da maturidade do processo.

---

## 10. Responsáveis do mecanismo — resumo do fluxo

| Etapa | Quem executa a ação | Quem valida o resultado |
|---|---|---|
| Escrever teste (RED) | Desenvolvedor/QA | JUnit/TestNG (engine de descoberta e execução) |
| Rodar teste e ver falha | Build tool (Maven/Gradle) | Relatório do runner (Surefire/Failsafe) |
| Implementar mínimo (GREEN) | Desenvolvedor/QA | Suíte de testes (execução automática) |
| Refatorar | Desenvolvedor/QA | Suíte de testes já existente (regressão) |
| Medir cobertura (complementar, não obrigatório) | JaCoCo ou equivalente | Time/QA, com leitura crítica, não literal |
| Validar fluxo N8N ponta a ponta | QA (fora do N8N, via RestAssured/HTTP) | Ambiente de staging + asserções de contrato |

---

### Nota de revisão
Este documento foi revisado internamente antes da entrega quanto a: (i) consistência entre a descrição teórica do ciclo e o exemplo de código Java apresentado; (ii) ausência de afirmações absolutas sobre benefícios de TDD, substituídas por ressalvas de contexto; (iii) coerência entre a seção de N8N e a limitação real da ferramenta em relação a testes automatizados nativos.