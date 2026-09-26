# Análise Descritiva no Power BI


## O que é Análise Descritiva?

A análise descritiva responde à pergunta:

> **"O que aconteceu?"**

Seu objetivo é transformar dados brutos em informações compreensíveis, resumindo eventos, comportamentos e resultados que já ocorreram.

No contexto do **Power BI**, a análise descritiva consiste em coletar, tratar, modelar e visualizar dados para identificar padrões históricos, tendências, volumes, indicadores e métricas do negócio.

---

# A Pirâmide da Análise de Dados

Normalmente, os tipos de análise são divididos em quatro níveis:

| Tipo de Análise | Pergunta Respondida |
|----------------|---------------------|
| Descritiva | O que aconteceu? |
| Diagnóstica | Por que aconteceu? |
| Preditiva | O que provavelmente acontecerá? |
| Prescritiva | O que devemos fazer? |

O Power BI é extremamente forte nos dois primeiros níveis:

- Descritivo
- Diagnóstico

---

# O Papel da Análise Descritiva

Imagine uma empresa de varejo.

A diretoria quer saber:

- Quanto vendeu no último trimestre
- Qual região faturou mais
- Quantos clientes compraram
- Quais produtos tiveram maior saída

Nesse momento não estamos tentando prever nada.

Apenas estamos descrevendo a realidade dos dados.

### Exemplos

✅ Faturamento total: R$ 5 milhões

✅ 12.000 pedidos realizados

✅ Região Norte representa 32% das vendas

✅ Produto A foi responsável por 18% do faturamento

Tudo isso é análise descritiva.

---

# Como o Power BI Executa Análise Descritiva

O processo normalmente passa por quatro camadas:

## 1. Coleta dos Dados

### Fontes comuns

- Excel
- SQL Server
- Oracle
- SAP
- APIs
- SharePoint
- CSV
- Dataverse

### Exemplo

```text
ERP → Power BI
CRM → Power BI
Planilhas → Power BI
```

---

## 2. Transformação dos Dados

Realizada principalmente no **Power Query**.

### Atividades comuns

- Remover duplicidades
- Corrigir erros
- Padronizar campos
- Criar colunas derivadas
- Unificar tabelas

### Exemplo

**Data Original**

| Data |
|--------|
| 01/01/24 |
| 2024-01-02 |

**Data Transformada**

| Data Padronizada |
|------------------|
| 01/01/2024 |
| 02/01/2024 |

---

## 3. Modelagem dos Dados

Após limpar os dados, criamos relacionamentos.

### Exemplo

```text
Dim_Clientes
      |
      |
Fato_Vendas
      |
      |
Dim_Produtos
```

### Modelo Estrela (Star Schema)

Composto por:

- Tabelas Fato
- Tabelas Dimensão

Essa estrutura melhora:

- Performance
- Escalabilidade
- Governança

---

## 4. Visualização

É aqui que a análise descritiva aparece para o usuário.

### Cartões (Cards)

Mostram KPIs.

**Exemplo**

```text
Receita Total
R$ 5.200.000
```

### Tabelas

Detalhamento dos registros.

| Produto | Vendas |
|----------|---------|
| Notebook | 500 |
| Mouse | 1200 |

### Matrizes

Permitem análises multidimensionais.

| Região | Janeiro | Fevereiro |
|---------|----------|------------|
| Norte | 100 | 120 |
| Sul | 80 | 95 |

### Gráficos de Barras

Comparações entre categorias.

```text
Produto A ████████
Produto B ██████
Produto C ███
```

### Gráficos de Linha

Análise temporal.

```text
Jan → Fev → Mar → Abr
```

Permitem identificar:

- Crescimento
- Queda
- Sazonalidade

### Mapas

Análise geográfica.

Perguntas respondidas:

- Onde vendo mais?
- Qual estado gera mais receita?
- Qual cidade tem menor performance?

---

# Métricas Utilizadas

Na análise descritiva utilizamos indicadores simples e objetivos.

## Soma

```DAX
Receita Total =
SUM(Vendas[ValorVenda])
```

## Quantidade

```DAX
Qtd Pedidos =
COUNT(Vendas[PedidoID])
```

## Média

```DAX
Ticket Médio =
AVERAGE(Vendas[ValorVenda])
```

## Percentual

```DAX
Participacao =
DIVIDE(
    [Receita Total],
    CALCULATE(
        [Receita Total],
        ALL(Produtos)
    )
)
```

## Acumulado

```DAX
Receita Acumulada =
TOTALYTD(
    [Receita Total],
    Calendario[Data]
)
```

---

# Principais Indicadores Descritivos

## Financeiro

- Receita
- Lucro
- Margem
- Custos
- Fluxo de caixa

## Comercial

- Faturamento
- Ticket médio
- Conversão
- Clientes ativos
- Clientes novos

## Logística

- Entregas
- Atrasos
- SLA
- Tempo médio de entrega

## RH

- Turnover
- Absenteísmo
- Headcount
- Horas extras

---

# Recursos de Power BI Utilizados

## Segmentação de Dados (Slicers)

Permitem filtrar rapidamente.

### Exemplo

- Ano
- Mês
- Região
- Produto

---

## Drill Down

Permite navegar entre níveis.

```text
Ano
 ↓
Trimestre
 ↓
Mês
 ↓
Dia
```

---

## Drill Through

Permite detalhar uma informação.

### Exemplo

O usuário clica na Região Norte e abre uma página exclusiva daquela região.

---

## Tooltips

Mostram informações adicionais ao passar o mouse.

### Exemplo

```text
Receita:
R$ 150.000

Pedidos:
400

Margem:
18%
```

---

# Exemplos Reais de Perguntas Respondidas

Uma dashboard descritiva consegue responder:

## Vendas

- Quanto vendemos?
- Quem vendeu mais?
- Em qual período houve maior faturamento?

## Marketing

- Quantos leads foram gerados?
- Qual campanha teve melhor resultado?

## Operação

- Quantos chamados foram atendidos?
- Qual equipe foi mais produtiva?

## Financeiro

- Qual foi o lucro mensal?
- Como está a evolução das despesas?

---

# Boas Práticas para Análise Descritiva no Power BI

## 1. Defina KPIs claros

### Ruim

```text
Mostrar tudo
```

### Bom

```text
Receita
Lucro
Margem
Ticket Médio
```

---

## 2. Use Modelo Estrela

Estrutura recomendada:

```text
Dim_Cliente
Dim_Produto
Dim_Calendario
       ↓
Fato_Vendas
```

---

## 3. Evite excesso de visuais

### Erro comum

```text
25 gráficos em uma única página
```

### Boa prática

```text
5 a 10 visuais relevantes
```

---

## 4. Priorize Storytelling

A dashboard deve contar uma história.

```text
KPIs
↓
Tendência
↓
Detalhamento
↓
Ações
```

---

## 5. Utilize Medidas DAX

Evite cálculos em colunas quando possível.

Prefira:

```DAX
Receita Total =
SUM(Vendas[Valor])
```

em vez de criar várias colunas calculadas desnecessárias.

---

# Limitações da Análise Descritiva

Ela mostra apenas o que aconteceu.

### Exemplo

```text
Vendas caíram 15%
```

A análise descritiva para aí.

Ela não responde:

❌ Por que caiu?

❌ O que acontecerá no próximo mês?

❌ O que devemos fazer?

Essas perguntas pertencem, respectivamente, à:

- Análise Diagnóstica
- Análise Preditiva
- Análise Prescritiva

---

# Resumo Executivo

Em **Power BI**, a análise descritiva é a prática de transformar dados históricos em informações visuais e indicadores de negócio para compreender o desempenho passado e atual da organização.

Ela utiliza recursos como:

- Power Query
- Modelagem dimensional
- DAX
- Dashboards interativos

para responder perguntas como:

- Quanto?
- Quando?
- Onde?
- Quem?

Servindo como base para análises mais avançadas e para a tomada de decisão orientada por dados.

## Forma simples de lembrar

> **Análise Descritiva no Power BI = Consolidar, organizar e visualizar dados históricos para entender claramente o que aconteceu no negócio.**