# Architecture Decision Record (ADR)

Um Architecture Decision Record (ADR) — ou Registro de Decisão de Arquitetura — é um documento curto e conciso criado para registrar uma escolha técnica significativa. Ele captura o problema, o contexto, as alternativas consideradas, a decisão tomada e as consequências dessa escolha. [1, 2] 
O principal objetivo de um ADR é garantir clareza e transparência, mantendo o "porquê" de uma decisão acessível a toda a equipe de engenharia e aos novos integrantes, evitando que conhecimentos importantes se percam com o tempo. [1, 3] 
Os ADRs são altamente valorizados na arquitetura moderna por seguirem princípios de documentação ágil e de "código vivo". [4] 
## 📋 Principais características de um ADR

* Leveza: Não é uma documentação extensa. Deve focar no essencial sobre a escolha feita. [5, 6] 
* Imutabilidade: Uma vez aceito, um ADR não deve ser editado. Se o cenário mudar e a decisão precisar ser revertida, um novo ADR é criado para substituir (superseder) o anterior, mantendo o histórico intacto. [7, 8] 
* Localização: Geralmente são salvos em formato Markdown (ex: .md) dentro do próprio repositório de código, próximos aos módulos que eles afetam. [9] 
* Autoria Descentralizada: Qualquer membro da equipe pode propor e redigir um ADR, desde que discuta com os envolvidos e registre o resultado. [2] 

## 🛠 Estrutura padrão de um ADR
Apesar de não ser um padrão rígido, o formato mais aceito (criado pelo arquiteto Michael Nygard) geralmente inclui as seguintes seções: [2, 10] 

   1. Título: Claro e descritivo (ex: ADR-001: Uso de mensageria RabbitMQ).
   2. Contexto: O cenário atual, os requisitos, o problema enfrentado e as forças ou restrições que motivaram a decisão.
   3. Opções/Alternativas: Quais foram as soluções consideradas e seus prós e contras.
   4. Decisão: A solução escolhida e a justificativa exata do porquê foi selecionada em relação às outras.
   5. Consequências: Os trade-offs, impactos de curto e longo prazo, e o que a equipe precisa aceitar ao seguir esse caminho.
   6. Status: O estado atual (ex: Proposto, Aceito, Rejeitado, Obsoleto/Substituído). [5, 7, 8, 11, 12] 

## 💡 Exemplo prático de uso
Imagine que seu time está discutindo onde salvar as sessões dos usuários. Após avaliar banco de dados em memória, Redis e cookies, a equipe escolhe Redis. Isso é documentado no ADR, que explica que o Contexto pedia alta velocidade com replicação; a opção Descartada foi o cookie por limite de tamanho; e a Consequência foi a necessidade de manter e gerenciar um cluster Redis dedicado. [5, 7] 
Se no futuro a arquitetura mudar para JWT (dispensando o Redis), um novo ADR será escrito como Substituído, contendo o link para o ADR original.
## ❓ Por que usar?

* Facilita o onboarding de novos membros, que passam a entender o histórico e o raciocínio por trás do sistema.
* Evita discussões repetitivas (o famoso "por que não usamos a tecnologia X?"), pois o registro já documenta as alternativas testadas.
* Evita a perda de conhecimento arquitetural quando membros da equipe deixam o projeto. [1, 2, 12, 13] 

Para explorar a criação e o versionamento desses registros, equipes costumam utilizar ferramentas de linha de comando como o [adr-tools no GitHub](https://github.com/npryce/adr-tools). [14] 
Você gostaria de escrever o seu primeiro ADR e precisa de ajuda com o template para uma decisão técnica específica? Ou quer entender a diferença entre ADRs e RFCs (Requests for Comments)?

[1] [https://medium.com](https://medium.com/@jhonywalkeer/guia-completo-sobre-architecture-decision-records-adr-defini%C3%A7%C3%A3o-e-melhores-pr%C3%A1ticas-f63e66d33e6)
[2] [https://incuca.net](https://incuca.net/en/adr-any-decision-record/)
[3] [https://marcdias.com.br](https://marcdias.com.br/adr-architecture-decision-records/)
[4] [https://www.youtube.com](https://www.youtube.com/watch?v=2u8GXr8VsmQ)
[5] [https://pt.linkedin.com](https://pt.linkedin.com/pulse/adr-chave-para-documenta%C3%A7%C3%B5es-%C3%A1geis-e-eficientes-de-bruno-tadeu-russo-ps5hf)
[6] [https://scrum-master.org](https://scrum-master.org/en/architecture-decision-record-how-and-why-use-adrs/)
[7] [https://learn.microsoft.com](https://learn.microsoft.com/en-us/azure/well-architected/architect-role/architecture-decision-record)
[8] [https://learn.microsoft.com](https://learn.microsoft.com/pt-br/azure/well-architected/architect-role/architecture-decision-record)
[9] [https://docs.cloud.google.com](https://docs.cloud.google.com/architecture/architecture-decision-records)
[10] [https://www.redhat.com](https://www.redhat.com/pt-br/blog/architecture-decision-records)
[11] https://adr.github.io
[12] [https://www.notion.com](https://www.notion.com/pt/templates/architecture-decision-records)
[13] [https://dev.to](https://dev.to/sertaoseracloud/como-criar-adrs-consistentes-em-arquiteturas-de-microsservicos-2moe)
[14] [https://medium.com](https://medium.com/@justhamade/architecting-the-future-planning-and-tooling-the-ai-driven-sdlc-334dcd02a4c9)



---


# Boilerplate
**Boilerplate** (ou *boilerplate code*) é o termo usado na programação para descrever **"código repetitivo"**. São aqueles trechos de código que você é obrigado a escrever em vários lugares do projeto para fazer coisas simples funcionarem, mas que não trazem nenhuma lógica de negócio real.

É o famoso "código padrão" ou "trabalho de datilógrafo" que todo programador precisa copiar e colar ou gerar automaticamente.

---

#### ❌ Com Boilerplate (Java Puro):

Para essa classe funcionar direito em uma API ou banco de dados, você precisa escrever tudo isso:

```java
public class Usuario {
    private Long id;
    private String nome;

    // Construtor Vazio (Boilerplate)
    public Usuario() {}

    // Construtor Cheio (Boilerplate)
    public Usuario(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters (MUITO Boilerplate)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    // Métodos equals, hashCode e toString (Mais boilerplate...)
}

```

O arquivo fica enorme, cheio de linhas que não dizem o que o sistema *faz*, apenas regras que a linguagem exige.

#### Sem Boilerplate (Com Lombok):

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nome;
}

```

---

### De onde veio esse nome estranho?

O termo veio da indústria de jornais do século XIX. As chapas de metal que continham anúncios ou textos que se repetiam em várias edições regionais (como colunas sindicais) eram chamadas de *"boilerplate"* (chapas de caldeira, porque eram duras e resistentes). Os jornais compravam essas chapas prontas para não precisarem digitar o mesmo texto letra por letra todos os dias.

A programação herdou o termo para designar esse código "comprado pronto" ou gerado no piloto automático.

### Outros exemplos de boilerplate:

* Configurações iniciais de servidores (HTML básico com as tags `<html>`, `<head>`, `<body>`).
* Conexões manuais com bancos de dados (abrir conexão, tratar erro, fechar conexão).

**Em resumo:** Boilerplate é a "burocracia" da linguagem de programação. Quanto menos boilerplate seu projeto tiver, mais limpo, legível e fácil de manter ele será!

---

# Unit
Unit (Unidade) é a menor parte do sistema cuja responsabilidade pode ser validada de forma isolada, independentemente de bancos de dados, APIs externas, sistema de arquivos, rede ou outros componentes da aplicação.

Testes unitários devem validar comportamento, não implementação.

# Padrão AAA
## Definição
O **Padrão AAA** é uma diretriz de design para estruturação de testes automatizados. Ele preconiza que cada caso de teste deve ser dividido visualmente e logicamente em três seções sequenciais e bem definidas: 
- **Arrange** (Organizar), 
- **Act** (Agir) e 
- **Assert** (Garantir).

## As Três Fases
1. **Arrange (Organizar/Preparar):** Configuração de todo o cenário necessário para o teste. Criação de dados de teste, inicialização de classes, injeção de *mocks* ou definição de estados iniciais.
2. **Act (Agir/Executar):** Execução da ação principal ou do método que está sendo testado. Idealmente, deve conter apenas **uma única linha** de código.
3. **Assert (Garantir/Verificar):** Verificação de que o resultado da ação executada condiz perfeitamente com o resultado esperado.


# Design Review
Um Design Review é um ponto de verificação (checkpoint) colaborativo onde designers, desenvolvedores, gerentes de produto e outras partes interessadas (stakeholders) avaliam uma proposta de design. 

O objetivo principal é identificar falhas precocemente, garantir o alinhamento com as metas do negócio, validar a viabilidade técnica e garantir a melhor experiência para o usuário final.

O Design Review (ou Revisão de Design) é um marco fundamental no desenvolvimento de produtos — sejam eles físicos, de software, jogos ou até mesmo experiências de usuário (UX).

Trata-se de um processo estruturado em que uma equipe se reúne para avaliar um projeto em andamento em relação aos seus requisitos, objetivos e viabilidade, antes de seguir para a próxima fase (como o desenvolvimento de código ou a fabricação).

Principais Objetivos
Identificação de Falhas Cedo: Encontrar problemas de usabilidade, lógica ou viabilidade técnica quando o custo de alteração ainda é baixo.

Alinhamento Multidisciplinar: Garantir que engenharia, produto e design estejam na mesma página.

Validação de Requisitos: Confirmar se a solução proposta realmente resolve o problema de negócio e atende às necessidades do usuário.

Consistência de Padrões: Garantir que o novo design siga o guia de estilos (Design System) ou os padrões de arquitetura da empresa.

Como Funciona na Prática (Etapas)
Preparação: O designer compartilha o protótipo, fluxos ou especificações com antecedência para que a equipe possa analisar antes da reunião.

Apresentação: O designer contextualiza o problema, apresenta a solução e explica as decisões de design (o "porquê" de cada elemento).

Feedback Estruturado: Os participantes fazem perguntas e apontam pontos de atenção.

💡 Nota: O foco deve ser sempre construtivo, focado no problema e nos dados, e nunca em preferências pessoais (evitando o famoso "eu não gostei dessa cor" sem uma justificativa técnica).

Definição de Próximos Passos: As críticas são documentadas e transformadas em tarefas de ajuste antes da aprovação final para o desenvolvimento.

Por que ele é crucial?
Em desenvolvimento de software (como no ecossistema Java, mobile ou VR), o custo de corrigir um erro de lógica de fluxo ou de interface após o código estar escrito é infinitamente maior do que ajustá-lo na fase de protótipo. O Design Review funciona como um "filtro" que economiza tempo precioso de desenvolvimento.

Gostaria de focar a definição em alguma área específica para o seu glossário, como Product Design (UX/UI) ou Engenharia de Software/Sistemas?


# BDD (Behavior Driven Development)
O BDD (ou Behavior-Driven Development — Desenvolvimento Guiado por Comportamento) é uma das metodologias mais consolidadas para unir a visão de negócios com a construção técnica de um software.

## O que é

BDD é uma abordagem de desenvolvimento que descreve o comportamento esperado do sistema antes da implementação.

## Estrutura

Para que qualquer pessoa (técnica ou não) consiga ler e escrever os cenários de BDD, utiliza-se uma sintaxe padrão conhecida como Gherkin. Ela é estruturada em três etapas principais:

- Dado (Given)
- Quando (When)
- Então (Then)

Dado que a palavra "Java" existe na base de conhecimento,

Quando o usuário perguntar "O que é Java?",

Então o sistema deverá retornar a definição cadastrada.



# TDD
O TDD (ou Test-Driven Development — Desenvolvimento Guiado por Testes) é um dos pilares mais importantes da engenharia de software moderna, focado em qualidade de código, design limpo e segurança ao realizar alterações no sistema.

Enquanto o BDD foca no comportamento do sistema sob a ótica do usuário e do negócio, o TDD foca na construção técnica e na lógica do código sob a ótica do desenvolvedor.

O TDD é uma técnica de desenvolvimento de software que inverte o processo tradicional de escrita de código: em vez de escrever a funcionalidade para depois testá-la, o desenvolvedor escreve o teste antes mesmo de criar o código da funcionalidade.O processo baseia-se em ciclos extremamente curtos e repetitivos de feedback, garantindo que cada pequena linha de código escrita seja automaticamente validada por um teste unitário.O Ciclo Red-Green-Refactor (Vermelho, Verde, Refatorar)O coração do TDD é um ciclo contínuo de três etapas bem definidas:1.1. Red (Escrever um Teste que Falha):Vermelho.Você escreve um teste unitário para uma pequena funcionalidade que ainda não existe. Ao rodar o teste, ele obrigatoriamente vai falhar (ficar vermelho) porque o código de produção ainda não foi escrito.2.2. Green (Fazer o Teste Passar):Verde.Você escreve a menor quantidade de código de produção necessária para que o teste passe (fique verde). O objetivo aqui não é o código perfeito, mas sim fazer o teste passar o mais rápido possível.3.3. Refactor (Melhorar o Código):Refatorar.Com o teste passando com segurança, você limpa e melhora o seu código (remove duplicidade, melhora nomes de variáveis, otimiza a lógica). O teste verde garante que você pode mexer na estrutura do código sem medo de quebrar a funcionalidade.Principais ObjetivosCódigo mais Limpo e Simples: Como você é forçado a pensar no teste primeiro, acaba escrevendo apenas o código estritamente necessário para atender àquele teste, evitando excessos (overengineering).Segurança para Refatorar: Com uma cobertura de testes robusta criada desde o primeiro dia, você pode alterar e atualizar o código no futuro sem medo de quebrar partes existentes do sistema.Menos Bugs em Produção: Os erros de lógica são detectados na máquina do desenvolvedor, segundos após serem criados, e não em fases avançadas ou pelo usuário final.Código Autodocumentado: Os próprios testes servem como uma especificação técnica legível de como as classes e métodos do sistema devem se comportar.💡 TDD vs. BDD: Qual a diferença?Para o seu glossário, é excelente ter essa distinção clara:TDD é sobre fazer as coisas do jeito certo (foco técnico, testes de unidade, qualidade do código interno, feito pelo desenvolvedor).BDD é sobre fazer as coisas certas (foco no negócio, testes de comportamento, garantia de que o sistema entrega o valor esperado, feito em colaboração com produto e QA).


# Edge Cases (casos de borda ou casos limite)
Um Edge Case é uma situação, cenário ou entrada de dados que ocorre no limite extremo dos parâmetros operacionais de um software. São situações que, embora sejam totalmente válidas e possíveis, acontecem fora do comportamento típico ou do "caminho feliz" (happy path) do usuário comum.

Se o TDD e o BDD nos ajudam a trilhar o "caminho feliz" (onde tudo funciona como o esperado), a busca pelos edge cases é o que impede o software de quebrar quando o mundo real apresenta situações extremas.

Se não forem previstos e testados, os edge cases costumam ser a maior fonte de travamentos (crashes), vulnerabilidades de segurança e bugs inesperados em produção.A Teoria dos Limites (Exemplo Visual)Imagine um sistema de validação de idade onde apenas pessoas com idade maior ou igual a 18 anos e menor ou igual a 120 anos podem se cadastrar.Caminho Feliz (Happy Path): O usuário digita 25 ou 40. O sistema funciona perfeitamente.Edge Cases (Os Limites): O que acontece se o usuário digitar exatamente 18 ou exatamente 120? E se digitar 17 ou 121? É exatamente nesses pontos de transição (as bordas) que a maioria dos erros de programação ocorre (como usar um operador > em vez de >=).Tipos Comuns de Edge CasesPara enriquecer o seu glossário, aqui estão as categorias mais comuns de casos de borda que os desenvolvedores e testadores de software precisam mapear:CategoriaExemplo de Edge CaseO que pode dar errado?Limites NuméricosTentar transferir $ 0,01 (valor mínimo) ou estourar o limite de variáveis (integer overflow).O sistema travar ou permitir transações inválidas.Entradas de TextoCampos de nome vazios, com caracteres especiais (@, !), emojis ou textos extremamente longos.Quebra de layout na interface ou falhas no banco de dados.Datas e HorasTransações feitas exatamente à meia-noite, anos bissextos (29 de fevereiro) ou mudanças de fuso horário/horário de verão.Agendamentos duplicados ou cobranças incorretas.Problemas de RedeO usuário perder a conexão com a internet exatamente no milissegundo em que clica em "Confirmar Pagamento".O pagamento ser processado, mas o usuário não receber a tela de confirmação (gerando duplicidade se ele clicar de novo).Diferença Importante: Edge Case vs. Corner CaseMuitas vezes esses termos são confundidos, mas há uma diferença de complexidade:Edge Case: Envolve uma variável ou parâmetro sendo levado ao limite (ex: tentar carregar um arquivo de exatamente 0 bytes).Corner Case: Envolve múltiplas condições/variáveis extremas acontecendo ao mesmo tempo (ex: tentar carregar um arquivo de 0 bytes, enquanto o servidor está sem espaço em disco e o usuário perde a conexão de rede simultaneamente).💡 Por que testar Edge Cases é vital?Mapear edge cases é o que diferencia um software "que funciona em ambiente de desenvolvimento" de um software "pronto para produção". No desenvolvimento guiado por testes (TDD), escrever testes unitários focados especificamente em testar as bordas (técnica conhecida como Análise de Valor Limite) é a melhor prática para blindar o código contra surpresas.


# Especificação (Specification)
Uma especificação descreve como um software deve se comportar.

Observe este exemplo:
Quando uma palavra conhecida existir na base de conhecimento, o sistema deverá retornar sua definição.



