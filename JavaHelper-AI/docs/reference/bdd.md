# Behavior Driven Development (BDD)

## O que é

BDD é uma abordagem de desenvolvimento que descreve o comportamento esperado do sistema antes da implementação.

O BDD (ou Behavior-Driven Development — Desenvolvimento Guiado por Comportamento) é uma das metodologias mais consolidadas para unir a visão de negócios com a construção técnica de um software.

## Estrutura

Dado (Given)

Quando (When)

Então (Then)

## Exemplo

Dado que a palavra "Java" existe na base de conhecimento,

Quando o usuário perguntar "O que é Java?",

Então o sistema deverá retornar a definição cadastrada.


Se você trabalha em equipes multidisciplinares (envolvendo pessoas de produto, design, desenvolvimento e testes), o BDD funciona como um "tradutor universal", garantindo que todos entendam exatamente o que o sistema deve fazer.

O BDD é uma prática de desenvolvimento de software ágil que incentiva a colaboração entre desenvolvedores, testadores (QA) e pessoas de negócios (como Product Owners ou clientes). Ele foca em definir o comportamento de um software por meio de exemplos práticos em linguagem natural (comum, não técnica) que, posteriormente, podem ser automatizados como testes de software.O BDD surgiu como uma evolução do TDD (Test-Driven Development), mudando o foco de "testar o código" para "testar o comportamento esperado do sistema".

## Gherkin
A Fórmula Mágica: Gherkin (Dado-Quando-Então). Para que qualquer pessoa (técnica ou não) consiga ler e escrever os cenários de BDD, utiliza-se uma sintaxe padrão conhecida como Gherkin. Ela é estruturada em três etapas principais:
- Dado (Given): O contexto ou cenário inicial (o estado do sistema).
- Quando (When): A ação que o usuário ou o sistema realiza.
- Então (Then): O resultado esperado após a ação.

Exemplo Prático (Transferência Bancária)Cenário: Transferência realizada com sucesso entre contasDado que o cliente tem $R\$\ 500,00$ de saldo na conta correnteE que a conta de destino é válidaQuando ele solicitar uma transferência de $R\$\ 200,00$Então o saldo da conta de origem deve passar a ser $R\$\ 300,00$E o sistema deve exibir uma mensagem de "Transferência realizada com sucesso"

Principais Objetivos
Linguagem Ubíqua (Comum): Eliminar ruídos de comunicação. Todo mundo usa os mesmos termos para descrever uma funcionalidade.Foco no Valor de Negócio: Garantir que a equipe de engenharia desenvolva apenas o que realmente gera valor e faz sentido para as regras de negócio.Documentação Viva: Como os cenários são escritos em texto simples e rodados como testes automatizados, eles funcionam como uma documentação do sistema que nunca fica desatualizada.

Como Funciona na Prática (O Fluxo)1.1. Descobrir e Conversar:Três Amigos (Product Owner, Dev e QA).A equipe se reúne para discutir uma funcionalidade. Em vez de focar no código ou no banco de dados, eles debatem exemplos reais de como o usuário usará o sistema.2.2. Formular os Cenários:Escrita em Gherkin.Esses exemplos práticos são escritos utilizando a estrutura Dado-Quando-Então.3.3. Automatizar e Desenvolver:Transformação em Testes.Os desenvolvedores usam frameworks (como Cucumber para Java/C# ou Behave para Python) para mapear o texto em Gherkin para testes automatizados. O código da funcionalidade é escrito até que todos esses testes passem ("fiquem verdes").

💡 Nota de Integração (Por que isso importa?)Ao contrário do que muitos pensam, BDD não é apenas uma ferramenta de testes (como o Cucumber). O BDD é, antes de tudo, um processo de comunicação. Ele evita o clássico cenário em que o desenvolvedor constrói uma solução perfeita e escalável, mas que resolve o problema errado porque ele não entendeu o que o negócio precisava.