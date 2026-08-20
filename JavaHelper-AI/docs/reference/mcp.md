# MCP

## Prompte inicial par MCP

Gostaria que incorporasse tudo sobre MCP. Em seguida, gostaria de explorar como eu poderia criar o meu próprio servidor MCP em Java para automatizar alguma tarefa do meu dia dia.


## MCP — o que é, sem enrolação

MCP é um protocolo aberto, baseado em JSON-RPC 2.0, que padroniza como uma aplicação de IA (o Host — ex: Claude Desktop, um IDE) se conecta a fontes de dados e ferramentas externas (o Server). O Host mantém um ou mais Clients, cada um ligado 1:1 a um Server.

Três primitivas centrais que um Server expõe:

Tools — funções que o modelo pode chamar (com JSON Schema de entrada). É o que você vai usar pra automação.
Resources — dados legíveis, endereçados por URI (arquivos, registros).
Prompts — templates reutilizáveis que o servidor oferece ao cliente.

E o cliente pode oferecer capacidades de volta ao servidor: sampling (pedir ao modelo do cliente pra gerar texto), elicitation (pedir input ao usuário) e roots (dizer em quais diretórios/URIs o servidor pode atuar).

O **Model Context Protocol (MCP)** é um padrão aberto que define uma forma universal de conectar Modelos de Linguagem (LLMs) a fontes de dados e ferramentas externas.

## O Conceito Fundamental
O MCP resolve o problema da integração customizada ("N x M"). Antes dele, cada aplicação de IA precisava de integrações específicas para cada base de dados ou ferramenta. Com o MCP, basta criar uma "ponte" uma única vez, e qualquer aplicação de IA que suporte o protocolo poderá utilizá-la.

Pense nele como o **"USB-C para a Inteligência Artificial"**: um padrão universal que permite conectar o "cérebro" da IA ao seu mundo real (arquivos, bancos de dados, APIs, terminal).

## Componentes da Arquitetura
1. **Host MCP (O Cliente):** O aplicativo que utiliza a IA (ex: Claude Desktop, Cursor, VS Code).
2. **Servidor MCP:** Um microsserviço que expõe funcionalidades para a IA.
3. **Transporte:** O meio pelo qual as mensagens fluem (geralmente via `stdio` para processos locais ou `HTTP/SSE` para serviços remotos).

## Principais Primitivas
* **Tools (Ferramentas):** Funções que a IA pode executar (ex: rodar uma query SQL, disparar um script, enviar uma mensagem).
* **Resources (Recursos):** Dados que a IA pode ler (ex: logs, conteúdos de arquivos, relatórios).
* **Prompts:** Templates reutilizáveis que facilitam interações específicas do usuário.

## Por que é importante?
* **Padrão Aberto:** Não depende de um único fornecedor. Qualquer desenvolvedor pode implementar um servidor ou cliente.
* **Padronizado:** Elimina a redundância. Uma vez criado um servidor MCP para um banco de dados, ele funciona em qualquer IA compatível.
* **Segurança:** O controle de acesso ao que a IA pode fazer ou ler fica centralizado e padronizado.

## Exemplo de Fluxo
1. **Servidor MCP:** Define uma ferramenta de "consultar_banco".
2. **Host:** Conecta ao servidor via `claude_desktop_config.json`.
3. **IA:** Quando o usuário pede para analisar dados, a IA chama a ferramenta padronizada, recebe a resposta estruturada e entrega o resultado final.

## Linguagens e Tecnologias para Criar um Servidor MCP

É possível criar servidores MCP utilizando diferentes linguagens, pois a Anthropic e a comunidade mantêm SDKs oficiais para várias tecnologias. A escolha depende do seu objetivo e ecossistema:

### 1. Python
* **Ideal para:** Automações rápidas, manipulação de arquivos, IA/ciência de dados e scripts focados em produtividade.
* **Vantagens:** Sintaxe limpa, ecossistema rico em bibliotecas de dados.

### 2. JavaScript / TypeScript (Node.js)
* **Ideal para:** Desenvolvedores web, aplicações baseadas em ecossistema Node e automações assíncronas.
* **Vantagens:** Forte presença na web e tipagem estática robusta com TypeScript.

### 3. Java (com Spring AI / Spring Boot)
* **Ideal para:** Ambientes corporativos, sistemas de missão crítica, bancos e operações de grande escala.
* **Vantagens:** 
  * Existe um **SDK Oficial em Java** (`modelcontextprotocol/java-sdk`) integrado ao **Spring AI**.
  * Permite usar anotações simples (como `@Tool`) para expor métodos de negócio.
  * Suporta tanto transporte via **STDIO** (para uso local) quanto **SSE (Server-Sent Events) / WebFlux** (para servidores web).

---



## SDK oficial do MCP

O **SDK oficial do MCP** (Software Development Kit) é o conjunto de bibliotecas fornecidas pelos mantenedores do padrão (originalmente criado pela Anthropic e hoje mantido como um padrão aberto da indústria) para facilitar a vida de quem quer programar servidores ou clientes MCP.

Em vez de você ter que programar tudo "na unha" a partir do zero, o SDK oficial faz o trabalho pesado de infraestrutura.

### O que o SDK oficial resolve para você?

Se não existisse um SDK, criar um servidor MCP exigiria que você implementasse manualmente o protocolo de baixo nível **JSON-RPC 2.0** e gerenciasse os fluxos de rede ou terminal. O SDK abstrai toda essa complexidade e entrega recursos prontos:

1. **Gestão de Transporte (Transport Layer):** Ele gerencia automaticamente a forma como as mensagens trafegam, seja localmente via **STDIO** (entrada e saída padrão do terminal, muito usado pelo Claude Desktop) ou via **HTTP/SSE / Streamable HTTP** para servidores web remotos.
2. **Conversão de Schemas (Serialização):** Ele traduz automaticamente o que você escreve no código (como uma função ou uma classe/record) para o formato JSON Schema que o modelo de linguagem (LLM) precisa ler para entender quais parâmetros a ferramenta exige.
3. **Negociação de Capacidades (Handshake):** Cuida da troca de mensagens inicial onde o cliente e o servidor dizem um ao outro quais recursos e ferramentas suportam.
4. **Tratamento de Erros e Assincronia:** Oferece estruturas prontas para lidar com exceções, timeouts e chamadas assíncronas de forma padronizada.

### Principais SDKs Oficiais Disponíveis

A comunidade e os mantenedores disponibilizam SDKs oficiais para as principais linguagens do mercado:

* **TypeScript / JavaScript:** O SDK de referência inicial, amplamente utilizado no ecossistema Node.js.
* **Python:** Muito popular para automações, integrações com IA, scripts de dados e ferramentas locais.
* **Java (`modelcontextprotocol/java-sdk`):** O SDK voltado para o ecossistema Java, que possui uma **integração nativa brilhante com o Spring AI**.

#### Como o SDK se manifesta no código (Exemplo do ecossistema Java/Spring):

Em vez de lidar com JSON-RPC, o SDK em Java com Spring AI permite que você crie ferramentas usando apenas anotações simples. Por exemplo:

```java
@Tool(description = "Consulta a situação financeira de um cliente no banco")
public ClienteStatus consultarCliente(@ToolParam(description = "CPF do cliente") String cpf) {
    // Sua lógica corporativa em Java aqui
    return servicoBanco.buscarPorCpf(cpf);
}

```

O SDK oficial pega esse método, mapeia os parâmetros automaticamente para o formato que a IA entende, e gerencia a comunicação por trás dos panos.


---
### O que envolve criar um Servidor MCP?

Criar um Servidor MCP envolve basicamente transformar as suas ferramentas ou fontes de dados internas em um formato que a inteligência artificial consiga compreender e acionar de forma segura. O processo envolve:

1. **Definição do Domínio (O que a IA vai fazer?):** Decidir quais capacidades você quer expor. Exemplo: consultar um banco de dados, ler arquivos de um diretório específico, chamar uma API corporativa ou rodar testes automatizados.
2. **Implementação das Primitivas:** Escrever o código que executa essas ações. Se você está criando uma ferramenta (`Tool`), você define o nome dela, uma descrição clara (para a IA saber *quando* usá-la) e o esquema dos parâmetros que ela exige.
3. **Configuração do Transporte:** Definir como o servidor vai se comunicar com o aplicativo de IA (o Host).
* Se for para uso local (ex: Claude Desktop no seu PC), usa-se o padrão **STDIO** (o servidor roda em segundo plano na sua máquina e conversa via terminal).
* Se for corporativo/remoto, usa-se **HTTP com SSE (Server-Sent Events)**.


4. **Registro no Host:** Configurar o arquivo JSON do aplicativo de IA (como o `claude_desktop_config.json`) para apontar o caminho do seu servidor.

---

### 2. O que é melhor: usar o SDK ou criar o servidor "do zero"?

A resposta direta e unânime no desenvolvimento de software é: **Sempre utilize o SDK oficial!**

* **O que significa criar "do zero"?**
Significa que você teria que implementar manualmente o protocolo de comunicação de baixo nível (**JSON-RPC 2.0**), gerenciar manualmente os fluxos de leitura e escrita de bytes no terminal (`stdin`/`stdout`), converter manualmente os tipos de dados para o formato rígido de *JSON Schema* que o LLM exige, além de programar o protocolo de inicialização (*handshake*) e tratamento de erros de rede. Seria reinventar a roda e gastar semanas em código de infraestrutura pura.
* **Por que usar o SDK?**
O SDK oficial abstrai toda essa complexidade de infraestrutura. Ele transforma a criação do servidor em algo muito parecido com o desenvolvimento de uma API REST tradicional.
* Com o SDK, você só precisa se preocupar com a **regra de negócio** (ex: escrever a função em Java/Python e colocar uma anotação dizendo *"esta função é uma Tool"*).
* O SDK cuida de todo o protocolo de comunicação por trás dos panos.



**Resumo:** O SDK *é* a ferramenta oficial construída justamente para você **criar** o seu servidor de forma limpa, rápida e sem erros de protocolo. Nunca tente implementar o MCP do zero, a menos que esteja criando um novo SDK para uma linguagem que ainda não é suportada!




---
*Nota: Criado pela Anthropic, mas mantido como um padrão aberto adotado pela indústria.*
"""
