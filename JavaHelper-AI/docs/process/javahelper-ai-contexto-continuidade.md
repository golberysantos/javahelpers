# JavaHelper AI — Contexto de Continuidade

## Objetivo

Preservar o contexto essencial da jornada do projeto **JavaHelper AI** para continuar em outro chat.

O projeto é também uma jornada prática de Engenharia de Software: além de construir a aplicação, o objetivo é compreender profundamente decisões, responsabilidades, arquitetura, testes, documentação e evolução.

## Dinâmica da colaboração

**Atlas — Tech Lead**
- Orienta a direção técnica.
- Propõe padrões e alternativas.
- Questiona decisões arquiteturais.
- Faz Design Reviews e Code Reviews.
- Explica o porquê das decisões.
- Pode fazer feedback direto sobre comportamentos profissionais quando necessário.

**Golbery — Software Engineer**
- Implementa decisões.
- Propõe alternativas.
- Questiona decisões.
- Explica seu raciocínio.
- Participa dos Reviews.
- Mantém documentação e artefatos atualizados.

A dinâmica deve continuar colaborativa, com liberdade para questionar. Questionamentos são parte do aprendizado, não confronto.

O usuário prefere críticas construtivas a elogios excessivos. O Tech Lead deve fazer revisões profundas e apontar problemas quando existirem.

## Princípios

Frase norte:

> "Código é um meio. Arquitetura é a forma como garantimos que esse código continue útil, compreensível e evolutivo ao longo do tempo."

Outras frases adotadas:

> "Arquitetura não é a arte de prever o futuro. É a arte de tornar o futuro menos doloroso."

> "O código mostra o que o sistema faz. A arquitetura explica por que ele foi construído dessa maneira. A documentação garante que esse conhecimento continue existindo mesmo quando os autores seguirem novos caminhos."

Práticas:
- Toda decisão arquitetural relevante deve deixar um registro.
- Toda Sprint começa com Sprint Planning documentado.
- A partir da Sprint 2, todo artefato possui identificador.
- Cada Sprint produz um conjunto de artefatos.
- Não seguimos metodologias por tradição; adotamos práticas porque ajudam a construir software melhor.
- Documentação deve ajudar alguém a tomar uma decisão ou entender o sistema.
- "Code is read more often than it is written."
- Nem todo pacote precisa necessariamente ter testes.
- Primeiro dominar JUnit 5; depois introduzir Mockito.
- Regra de Engenharia nº 1: "Nunca estudar cansado apenas para cumprir a Sprint."

## Metodologias

O usuário gosta de:
- Scrum
- ICONIX
- RUP (Rational Unified Process)

A jornada aproveita conceitos de artefatos, visão, missão, valores, glossário, arquitetura, decisões, requisitos e ciclos incrementais, sem aplicar metodologia de maneira burocrática.

## Estrutura de documentação

O projeto possui `docs/`, incluindo:

```text
docs
├── adr
├── architecture
├── diagrams
├── api
├── tutorials
├── decisions
├── philosophy
├── process
├── reference
├── roadmap
└── templates
```

Documentos mencionados/criados:
- `manifesto.md`
- `vision.md`
- `mission.md`
- `values.md`
- `learning-path.md`
- `glossary.md`
- `scrum-guide.md`
- `pull-request-checklist.md`
- `reference-archive`
- `docs/reference/testing.md`
- `docs/reference/bdd.md`
- documentação sobre Jackson
- documentação sobre ciclo de requisição Spring
- ADRs e decisões arquiteturais
- `DEC-001`
- `DEC-002`
- `ADR-002`

A partir da Sprint 2, os artefatos devem possuir identificador.

## Sprints

### Sprint 1
Registrada em:

```text
sprints/SPR-001.md
```

### Sprint 2

Registrada em:

```text
sprints/SPR-002.md
```

Inclui Sprint Planning, Marco Técnico, cultura de testes, JUnit 5, Code Review, BDD e documentação de referência.

## JavaHelper AI

Visão:

> O JavaHelper AI deverá ser uma API capaz de auxiliar desenvolvedores Java durante o processo de aprendizagem e desenvolvimento de software, utilizando Inteligência Artificial e boas práticas de Engenharia de Software.

O projeto será publicado no GitHub como parte do portfólio.

Contexto atual:
- Java
- Spring Boot
- Maven
- JUnit 5
- Mockito posteriormente
- Jackson
- Spring MVC
- Clean Architecture + DDD como direção arquitetural
- futura integração com OpenAI
- conhecimento local/mock para desenvolvimento e testes sem dependências externas

O projeto foi criado pelo Spring Initializr.

## Estrutura inicial de pacotes

Foi criada inicialmente:

```text
br.com.javahelperai
├── config
├── controller
├── dto
├── entity
├── repository
├── service
├── exception
├── util
├── knowledge
├── prompt
├── security
├── cache
└── ai
```

Também foi discutido **Package-by-Feature**, preferência arquitetural do usuário, com cada funcionalidade encapsulando seus componentes.

Foi aceita uma evolução arquitetural que combina organização por funcionalidade com princípios de Clean Architecture quando a complexidade justificar.

## Clean Architecture + DDD

Direção:

```text
Infrastructure
        │
Presentation
        │
Application
        │
Domain
```

**Infrastructure** — tecnologia externa: banco, frameworks, HTTP clients, filas, arquivos, OpenAI, PostgreSQL.

**Presentation** — entrada/saída: Controllers, HTTP, JSON, DTOs, respostas.

**Application** — casos de uso e orquestração.

**Domain** — regras de negócio, entidades, Value Objects e contratos, sem dependências tecnológicas.

### Regra da Dependência

As dependências apontam para dentro. O Domain não conhece Infrastructure ou Presentation.

## Classes e responsabilidades

### ChatController

Presentation. Recebe requisições HTTP, transforma entrada em chamada para a aplicação e devolve resposta.

Não deve responder à pergunta diretamente.

### ChatService

Interface:

```java
package br.com.javahelperai.service;

public interface ChatService {

    String perguntar(String pergunta);

}
```

Application. Define a capacidade/caso de uso "perguntar".

Implementações discutidas:
- `MockChatService`
- futura implementação OpenAI, como `OpenAIChatService`/`GptChatService`

### MockChatService

Foi ajustado para:

```java
public class MockChatService implements ChatService
```

Possui:

```java
private final KnowledgeBase knowledgeBase;
```

Essa construção foi entendida como Injeção de Dependência.

### KnowledgeBase

Contrato:

```java
public interface KnowledgeBase {

    String buscarResposta(String pergunta);

}
```

Pode mudar quando o requisito do sistema mudar, por exemplo:

```java
List<String> buscarRespostas(String pergunta);
```

ou:

```java
RespostaIA buscarResposta(String pergunta);
```

Isso é diferente de mudar a interface apenas porque a tecnologia de persistência mudou.

### MockKnowledgeBase

Implementação atual baseada em `knowledge-base.json`. Ela carrega um `Map<String,String>` com Jackson e procura a primeira palavra-chave contida na pergunta.

Foi discutido que `MockKnowledgeBase` pode ser uma nomenclatura imprecisa se a classe realmente representa uma base carregada de JSON. Alternativas futuras:
- `JsonKnowledgeBase`
- `FileKnowledgeBase`

Um mock verdadeiro poderia ser criado especificamente pelos testes.

## Inversão de Dependência

Modelo:

```text
KnowledgeBase (interface)
          ▲
          │
   ┌──────┴──────────────┐
   │                     │
MockKnowledgeBase   OpenAIKnowledgeBase
```

A interface define o contrato; implementações podem variar.

A arquitetura busca permitir trocar implementações sem acoplar o consumidor à tecnologia.

## Default Methods

Foi discutido por que `default methods` não são a estratégia favorita para evolução de contratos:
- podem esconder evolução do contrato
- podem aumentar complexidade
- podem mascarar decisões de design
- não substituem contratos claros

`DEC-002` foi criada e aceita.

Consequências positivas registradas:
- evolução incremental
- maior estabilidade
- melhor reutilização
- facilidade de testes
- menor custo de manutenção

## KnowledgeBaseSelector

Foi discutida uma possível classe `KnowledgeBaseSelector`. O usuário inicialmente sugeriu uma classe separada para evitar alterar o que já existia, mas depois concordou que `ChatService` poderia assumir o papel de aplicação/orquestração quando adequado.

Injeção de Dependência:
- a classe declara a dependência
- Spring/código de composição injeta a implementação
- o consumidor fica desacoplado da implementação concreta

## Testes

Sprint 2 estabeleceu cultura de testes.

Ordem:
1. JUnit 5
2. Mockito posteriormente

A estrutura de testes espelha `src/main/java`.

Primeiro teste: `Calculadora`, usando AAA:
- Arrange: instancia `Calculadora`
- Act: executa `somar`
- Assert: verifica resultado com `assertEquals`

Se `somar()` retornasse `a - b`, o teste deveria falhar.

## Testes da KnowledgeBase

Base discutida:

```json
{
  "polimorfismo": "Polimorfismo é a capacidade de um objeto assumir diferentes comportamentos através da herança e interfaces.",
  "herança": "Herança permite reutilizar atributos e métodos de outra classe.",
  "encapsulamento": "Encapsulamento protege os atributos utilizando getters, setters e modificadores de acesso.",
  "interface": "Uma interface define um contrato que deve ser implementado pelas classes."
}
```

Cenários:
- palavra-chave conhecida
- ausência de palavra-chave
- variações de texto
- pergunta contendo mais de uma palavra-chave

`assertNull()` representa explicitamente "nenhuma resposta encontrada".

Princípio importante: testes protegem comportamento/especificação, não detalhes de implementação. Assim, trocar `knowledge-base.json` por PostgreSQL não deveria exigir mudanças nos testes de comportamento se a especificação continuar igual.

## BDD e Design Review

Foi criado `docs/reference/bdd.md`.

A abordagem é definir primeiro:
- problema
- comportamento esperado
- cenários
- decisões

antes da implementação.

O usuário percebeu a ligação com BDD/Gherkin.

## OpenAI

Para a versão 1.0, a escolha conceitual é utilizar a API da OpenAI, pois possui estrutura pronta para consumo.

Porém, o usuário não possui orçamento para manter uma chave OpenAI. Portanto:
- manter mock/base local
- preparar arquitetura para futura integração
- não exigir serviço externo nos testes
- manter projeto executável sem chave

Alternativas futuras discutidas:
- busca textual
- search engines
- Spring AI
- Lucene
- Elasticsearch
- OpenSearch

## Jackson

Foi criado material específico sobre Jackson.

Desserialização:

```text
JSON
 ↓
Jackson
 ↓
PerguntaDTO
```

Serialização:

```text
RespostaDTO
 ↓
Jackson
 ↓
JSON
```

O ponto de autoconfiguração de Jackson no Application Context ainda pode ser aprofundado.

## Spring MVC — ciclo da requisição

Fluxo estudado:

```text
HTTP Request
     │
     ▼
DispatcherServlet
     │
     ▼
HandlerMapping
     │
     ▼
ChatController.perguntar()
     │
     │ @RequestBody
     ▼
HttpMessageConverter
     │
     ▼
Jackson
     │
     ▼
PerguntaDTO
```

Saída:

```text
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

Esse fluxo é um modelo didático simplificado; existem outras etapas internas no Spring MVC.

### Annotations

`@RequestMapping("/chat")`
- define o caminho-base.

`@PostMapping`
- define que o método atende POST compatível com o caminho.

Resultado:

```text
POST /chat
    ↓
perguntar()
```

`@RequestBody`
- indica que o corpo HTTP deve ser associado ao parâmetro.
- não é o conversor.

`@RestController`
- incorpora o comportamento de `@ResponseBody` nos métodos.

`@ResponseBody`
- indica que o retorno deve ser tratado como corpo da resposta HTTP.
- não é o conversor JSON.

`HttpMessageConverter`
- integra a conversão entre HTTP e objetos Java.

Jackson:
- realiza serialização/desserialização JSON.

`HandlerMapping`:
- participa do mapeamento da requisição para o Controller/método apropriado.

## Content-Type e Accept

**Content-Type**
- informa o formato do conteúdo enviado.

```http
Content-Type: application/json
```

**Accept**
- informa o formato que o cliente deseja receber.

```http
Accept: application/json
```

Memorização:

```text
Content-Type
    ↓
o que estou enviando?

Accept
    ↓
o que quero receber?
```

## Quiz SB-005

Foi realizado um quiz sobre:
- Content-Type
- Accept
- RequestMapping
- PostMapping
- RestController
- ResponseBody
- RequestBody
- HttpMessageConverter
- Jackson
- DispatcherServlet
- HandlerMapping
- fluxo de entrada e saída

O usuário demonstrou domínio dos conceitos. Houve uma resposta digitada incorretamente na última questão; o usuário esclareceu que conhecia a resposta correta. Não tratar como falha conceitual.

## Próximo passo

1. Atualizar/criar:
   `docs/reference/spring-request-lifecycle.md`
2. Registrar o fluxo HTTP/Spring MVC estudado.
3. Fazer Design Review do `ChatController`.
4. Investigar quais comportamentos deixam de existir quando annotations são removidas.
5. Continuar conectando annotations, Spring Container, Spring MVC, HTTP, Jackson, DTOs, Controllers, Application, Domain e Infrastructure.

## Estilo de ensino

O usuário gosta quando o Tech Lead:
- faz perguntas antes de entregar respostas
- cria pequenos desafios
- usa quizzes
- faz Design Reviews
- questiona decisões
- apresenta alternativas
- explica consequências
- conecta teoria com código real
- aponta erros diretamente
- não evita críticas por receio de parecer excessivamente crítico
- usa analogias quando ajudam, mas volta ao mecanismo técnico
- evita transformar toda interação em uma lista burocrática de registros

Objetivo: o Software Engineer deve conseguir abrir qualquer classe e explicar:
- por que ela existe
- por que está naquele pacote/camada
- por que aquele padrão foi escolhido
- por que outra solução não foi escolhida
- como a decisão afeta a arquitetura
- quais mudanças futuras ela facilita ou dificulta

## Frases de continuidade

> "A melhor arquitetura é aquela que permite mudar de ideia com o menor custo possível."

> "Toda decisão arquitetural relevante deve deixar um registro."

# FIM DO CONTEXTO
