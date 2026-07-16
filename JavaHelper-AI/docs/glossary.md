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



