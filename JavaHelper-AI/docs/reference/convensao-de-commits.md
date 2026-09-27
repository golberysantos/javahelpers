# Convenção de Commits e Pull Requests

Este guia padroniza commits, branches e descrições de PRs para manter o histórico do projeto claro, consistente e fácil de revisar.

---

## Objetivo

- facilitar a leitura do histórico de alterações;
- melhorar a revisão de código;
- manter o processo de desenvolvimento mais previsível;
- reduzir ambiguidades em mudanças de funcionalidade, correção e documentação.

---

## Princípios gerais

- cada commit deve ter uma responsabilidade única;
- o commit deve descrever o que foi alterado e por quê;
- evite misturar correção, refatoração e documentação no mesmo commit;
- prefira mensagens curtas, claras e em inglês ou português de forma consistente ao longo do projeto;
- escreva em modo imperativo: “adiciona”, “corrige”, “remove”, “atualiza”.

---

## Convenção de commits (Conventional Commits)

### Tipos mais utilizados

- **feat:** adiciona uma nova funcionalidade;
- **fix:** corrige um bug ou falha;
- **docs:** altera documentação;
- **style:** ajusta formatação, identação ou organização visual sem mudar lógica;
- **refactor:** reorganiza código e melhora a estrutura sem mudar comportamento;
- **test:** adiciona ou altera testes;
- **chore:** manutenção geral, configuração ou tarefas operacionais;
- **build:** altera o processo de build ou dependências do projeto;
- **ci:** ajustes em pipeline, CI/CD ou automação de integração;
- **perf:** melhora desempenho;
- **revert:** desfaz um commit anterior.

### Estrutura recomendada

```bash
tipo(escopo): descrição curta
```

Exemplos:

```bash
feat(auth): adiciona login com OAuth
fix(api): corrige erro ao validar payload
docs(readme): atualiza instruções de instalação
refactor(service): simplifica fluxo de autenticação
```

### Boas práticas

- use uma descrição curta e objetiva;
- evite frases longas ou genéricas como “ajustes finais”;
- se necessário, complemente com corpo do commit explicando contexto e impacto;
- mantenha o assunto principal em uma linha.

---

## Convenção para nomes de branches

Use prefixos consistentes para indicar a intenção da mudança:

- **feature/** – nova funcionalidade;
- **fix/** ou **bugfix/** – correção de bug;
- **hotfix/** – correção urgente em produção;
- **refactor/** – melhoria estrutural do código;
- **docs/** – documentação;
- **test/** – criação ou ajuste de testes;
- **chore/** – manutenção e configuração;
- **build/** – alterações no build;
- **ci/** – ajustes de integração contínua.

Exemplos:

```bash
feature/login-oauth
fix/validation-payload
docs/architecture-network
refactor/service-layer
```

---

## Mensagem de PR (Pull Request)

Mensagem de PR é a descrição do que foi alterado em um pull request, explicando contexto, objetivo e impacto da mudança.

Ela deve responder, de forma clara, três perguntas:

1. O que foi alterado?
2. Por que foi alterado?
3. Como foi validado?

### Estrutura recomendada

```md
## Objetivo
Descreva o problema ou necessidade que motivou a alteração.

## O que foi feito
- item 1
- item 2
- item 3

## Impacto
Explique o efeito esperado na aplicação, na equipe ou no processo.

## Validação
- testes executados
- checagens realizadas
- observações relevantes
```

### Exemplo

```md
## Objetivo
Atualizar a documentação do mini datacenter para refletir a nova topologia com a workstation como bastion host.

## O que foi feito
- ajustei a descrição dos IPs e interfaces da workstation;
- alinhei o fluxo de acesso ao PostgreSQL via SSH tunnel;
- atualizei os diagramas de rede e o fluxo de administração;
- documentei o uso do DBeaver e do Power BI no acesso ao banco.

## Impacto
A documentação agora acompanha a arquitetura real do ambiente, reduzindo confusão entre rede interna e externa e padronizando o acesso administrativo.

## Validação
- revisão dos diagramas e dos endereços de rede;
- conferência do fluxo bastion host → PostgreSQL;
- alinhamento com o guia de SSH Tunnel já documentado.
```

---

## Boas práticas para PRs

- escreva em linguagem objetiva e direta;
- use listas claras para descrever mudanças;
- mencione dependências, riscos e impactos quando houver;
- inclua testes executados ou validações feitas;
- não use textos genéricos como “ajustes finais” ou “melhorias diversas”; 
- mantenha o PR focado em uma mudança principal por vez.

---

## Resumo

Uma boa convenção de commits e PRs melhora a manutenção do projeto, facilita a revisão por pares e reduz o risco de regressões. O mais importante é manter a padronização, a clareza e a objetividade em cada alteração.

