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


## SDK oficial do MCP
---
*Nota: Criado pela Anthropic, mas mantido como um padrão aberto adotado pela indústria.*
"""
