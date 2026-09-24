# Pipelines

## 1. O que é um Pipeline?

**Pipeline** (em português, "duto" ou "tubulação") é uma sequência de etapas automatizadas e encadeadas para processar dados, código ou tarefas. A saída de uma etapa serve como entrada para a próxima.

O conceito vem da engenharia e da computação, inspirado em linhas de montagem industriais: cada estágio faz uma parte do trabalho, de forma padronizada e repetível.

> **Definição formal:** "Um pipeline é um conjunto de elementos de processamento de dados conectados em série, onde a saída de um elemento é a entrada do próximo." (Adaptado de Kim, Humble & Debois, *The DevOps Handbook*, 2016)

## 2. Para que serve?

1.  **Automação:** Elimina tarefas manuais repetitivas.
2.  **Padronização:** Garante que o mesmo processo ocorra sempre da mesma forma.
3.  **Rastreabilidade:** Permite auditar cada etapa.
4.  **Escalabilidade:** Lida com grandes volumes sem intervenção humana.

## 3. Principais Tipos de Pipelines

### 3.1. Pipeline de CI/CD (Integração e Entrega Contínua)
O mais famoso no desenvolvimento de software.

**Etapas típicas:**
`Commit -> Build -> Test -> Deploy em Staging -> Deploy em Produção`

- **CI (Continuous Integration):** Integra o código de vários devs várias vezes ao dia, rodando testes automáticos.
- **CD (Continuous Delivery/Deployment):** Entrega o código testado para produção de forma automática ou semi-automática.

Ferramentas: GitHub Actions, GitLab CI, Jenkins, CircleCI, Azure DevOps.

### 3.2. Pipeline de Dados (Data Pipeline)
Move e transforma dados de uma fonte para um destino.

**Etapas típicas (ETL/ELT):**
`Extração -> Transformação -> Carregamento`

Exemplo: Extrair vendas do banco de dados, limpar dados nulos, carregar no Data Warehouse para o dashboard.

Ferramentas: Apache Airflow, dbt, Apache Spark, Fivetran.

### 3.3. Pipeline de Machine Learning (MLOps)
Versão especializada do pipeline de dados para ML.

**Etapas típicas:**
`Coleta de Dados -> Pré-processamento -> Treinamento -> Validação -> Deploy do Modelo -> Monitoramento`

Ferramentas: Kubeflow, MLflow, SageMaker Pipelines.

## 4. Principais Dúvidas (FAQ)

### Dúvida 1: Pipeline é a mesma coisa que automação?
**Não.** Toda pipeline é uma automação, mas nem toda automação é uma pipeline. A automação pode ser uma tarefa única (ex: ligar a luz às 18h). A pipeline implica *encadeamento* e *dependência* entre etapas.

### Dúvida 2: Qual a diferença entre CI, CD e Pipeline?
- **Pipeline** é a estrutura (o cano).
- **CI/CD** é o *uso* dessa estrutura para um fim específico (entregar software).
Pense assim: pipeline é a esteira; CI/CD é o que você produz nela.

### Dúvida 3: O que acontece se uma etapa falhar?
A pipeline para (fail-fast). Isso é intencional. Ela impede que um erro se propague. Bons pipelines enviam alertas (Slack, e-mail) e permitem *retry* apenas da etapa que falhou, sem recomeçar do zero.

### Dúvida 4: Pipeline declarativa vs. imperativa?
- **Declarativa:** Você descreve *o que* quer (ex: `quero 3 testes`). Mais simples. Ex: GitHub Actions em YAML.
- **Imperativa:** Você descreve *como* fazer passo a passo (ex: script Groovy no Jenkins). Mais flexível, porém mais complexa.

### Dúvida 5: Como começar um pipeline do zero?
1. Mapeie o processo manual atual no papel.
2. Comece pequeno: automatize 2-3 etapas críticas (ex: só build + teste).
3. Versione sua pipeline como código (Pipeline as Code).
4. Adicione observabilidade (logs claros em cada etapa).

## 5. Boas Práticas Essenciais

- **Pipeline as Code:** Defina a pipeline em um arquivo (ex: `.yml`) versionado no Git, não em cliques na interface.
- **Idempotência:** Rodar a pipeline 2x deve gerar o mesmo resultado.
- **Feedback Rápido:** Etapas rápidas (lint, testes unitários) primeiro. Testes lentos depois.
- **Segurança (DevSecOps):** Não coloque senhas no código. Use *secrets* gerenciados.

## 6. Citações e Referências para Estudo

1.  **FOWLER, Martin.** *Continuous Integration.* 2006. Artigo seminal que definiu as práticas de CI. Disponível em: martinfowler.com
    > "A integração contínua é uma prática de desenvolvimento de software onde os membros de uma equipe integram seu trabalho frequentemente..."

2.  **KIM, Gene; HUMBLE, Jez; DEBOIS, Patrick; WILLIS, John.** *The DevOps Handbook: How to Create World-Class Agility, Reliability, and Security in Technology Organizations.* IT Revolution Press, 2016.
    > Referência principal sobre fluxo de trabalho e pipelines de entrega.

3.  **HUMBLE, Jez; FARLEY, David.** *Continuous Delivery: Reliable Software Releases through Build, Test, and Deployment Automation.* Addison-Wesley, 2010.
    > O livro definitivo sobre pipelines de deployment.

4.  **Documentação Oficial:**
    - GitHub Actions Docs: docs.github.com/actions
    - Apache Airflow Docs: airflow.apache.org/docs

---
*Criado para consulta rápida. Última revisão: Set/2026.*