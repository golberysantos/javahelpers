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

