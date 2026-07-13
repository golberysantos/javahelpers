# Estratégia de Testes

## Testes Unitários

Objetivo:
Validar uma única classe isoladamente.

Ferramentas:
- JUnit 5
- Mockito

---

## Testes de Integração

Objetivo:
Validar a integração entre componentes Spring.

Ferramentas:
- Spring Boot Test
- MockMvc

---

## Cobertura

Ferramenta:
JaCoCo

Meta inicial:
80%

Meta futura:
90%


Uma pequena convenção
Gostaria de definir um padrão para os testes.

src

test

java

br.com.javahelperai

service

MockChatServiceTest.java

controller

ChatControllerTest.java



Nosso primeiro Code Review

Quero que ele seja especial.

Quando criarmos o primeiro teste com Mockito, faremos um review não apenas do código, mas da intenção por trás dele.

Vamos analisar perguntas como:

Esse teste é fácil de entender?
O nome descreve claramente o comportamento esperado?
Estamos testando comportamento ou implementação?
O teste continua válido se a implementação interna mudar?

Essas questões ajudam a escrever testes mais robustos e menos acoplados.


## Convenção de nomenclatura

Classe: ChatService.java
Teste: ChatServiceTest.java

Classe: PromptBuilder.java
Teste: PromptBuilderTest.java



## FIRST

É um acrônimo muito famoso para testes unitários.

Um bom teste deve ser:

F

Fast

Rápido.

I

Independent

Independente.

Um teste não depende do outro.

R

Repeatable

Repetível.

O resultado deve ser sempre o mesmo.

S

Self-validating

O próprio teste informa se passou ou falhou.

Não precisa alguém interpretar.

T

Timely

Escrito no momento adequado.

Nem muito tarde.

Nem muito cedo.


## Estrutura de um teste (AAA)

Todo teste unitário deve ser organizado em três etapas:

- Arrange: preparar o cenário.
- Act: executar a ação.
- Assert: verificar o resultado.

Esse padrão melhora a legibilidade e facilita a manutenção dos testes.


O Arrange corresponde à preparação do cenário necessário para executar o teste.

O Act corresponde à execução da funcionalidade que desejamos validar.

Assert verifica se o valor desejado foi alcançado.
