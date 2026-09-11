
#  Gherkin

No ecossistema Java (e em outras linguagens), o **Gherkin** serve para escrever especificações de comportamento de software e cenários de teste em uma **linguagem natural, legível por humanos** (usando uma estrutura padronizada em inglês ou português).

Ele é a base da abordagem **BDD (Behavior-Driven Development** ou Desenvolvimento Orientado por Comportamento) e, em projetos Java, trabalha em conjunto com ferramentas como o **Cucumber** ou **JBehave**.

Em resumo, no Java o Gherkin serve para:

### 1. Servir como "Ponte" de Comunicação

Permite que analistas de negócios, product owners (POs), testadores (QA) e desenvolvedores Java escrevam e entendam as regras de negócio da mesma forma, eliminando ambiguidades.

### 2. Automatizar Testes de Aceitação

Os arquivos do Gherkin (salvos com a extensão `.feature`) são lidos por frameworks em Java (como o Cucumber-JVM). O framework pega frases escritas em linguagem natural e as "conecta" a trechos de código Java (os chamados *Step Definitions*), executando os testes de forma automatizada.

### 3. Funcionar como Documentação Viva (*Living Documentation*)

Como os arquivos `.feature` descrevem o que o software faz de forma limpa e atualizada, eles servem como uma documentação técnica e de negócios que nunca fica obsoleta, pois se estiver errada, os testes automatizados falham.

---

### Exemplo Prático no contexto Java

Um arquivo de texto chamado `login.feature` escrito em Gherkin:

```gherkin
Funcionalidade: Autenticação de Usuário
  Como um cliente do banco
  Quero fazer login no sistema
  Para acessar minha conta

  Cenário: Login com sucesso
    Dado que o usuário está na página de login
    Quando ele preenche o campo de e-mail com "golbery@email.com" e a senha com "123456"
    E clica no botão de entrar
    Então o sistema deve exibir o painel principal com a mensagem "Bem-vindo, Golbery"

```

No seu projeto Java, você criará uma classe de *Step Definitions* para mapear cada uma dessas frases para código Java real (usando Selenium, RestAssured ou regras de negócio puras):

```java
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;

public class LoginSteps {

    @Dado("que o usuário está na página de login")
    public void que_o_usuario_esta_na_pagina_de_login() {
        // Código em Java para abrir a página de login
    }

    @Quando("ele preenche o campo de e-mail com {string} e a senha com {string}")
    public void ele_preenche_o_campo_de_email_com_e_a_senha_com(String email, String senha) {
        // Código em Java para preencher os inputs
    }

    @Quando("clica no botão de entrar")
    public void clica_no_botao_de_entrar() {
        // Código em Java para clicar no botão
    }

    @Então("o sistema deve exibir o painel principal com a mensagem {string}")
    public void o_sistema_deve_exibir_o_painel_principal_com_a_mensagem(String mensagemEsperada) {
        // Código em Java para validar o resultado (Asserts)
    }
}

```