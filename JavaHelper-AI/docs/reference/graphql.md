O **GraphQL** é uma linguagem de consulta (*query language*) e um ambiente de execução (*runtime*) para APIs, criada pelo Facebook em 2012 e tornada código aberto em 2015.

A principal ideia por trás do GraphQL é dar ao cliente (aplicativo mobile, site, etc.) o poder de **pedir exatamente os dados de que precisa** — nem mais, nem menos.

---

## 💡 Qual problema ele resolve?

Em APIs REST tradicionais, o backend define a estrutura das respostas e os *endpoints* (como `/users/1` ou `/posts`). Isso gera dois problemas comuns:

* **Under-fetching:** Para carregar um perfil de usuário com seus posts, você precisa fazer uma chamada para `/users/1` e outra para `/users/1/posts`.
* **Over-fetching:** A API de `/users/1` devolve 30 campos (nome, e-mail, endereço, data de criação, etc.), mas o app só precisava do **nome**.

O GraphQL resolve isso usando um **único endpoint** (geralmente `/graphql`) onde você faz requisições declarativas.

---

## ⚔️ GraphQL vs. REST

| Conceito | REST | GraphQL |
| --- | --- | --- |
| **Endpoints** | Múltiplos (`/users`, `/posts`) | Apenas um (`/graphql`) |
| **Formato da Resposta** | Definido pelo Backend | Definido pelo Cliente |
| **Recuperação de Dados** | Múltiplas requisições para dados conectados | Uma única requisição para dados aninhados |
| **Tipagem** | Opcional / Depende de documentação | Forte (via *Schema*) |

---

## 🧱 Como funciona na prática?

A arquitetura do GraphQL gira em torno de três pilares:

### 1. Schema e Tipos

O backend define o contrato de dados usando o **Schema Definition Language (SDL)**:

```graphql
type Usuario {
  id: ID!
  nome: String!
  email: String!
  posts: [Post]
}

type Post {
  id: ID!
  titulo: String!
}

type Query {
  obterUsuario(id: ID!): Usuario
}

```

### 2. Operações principais

* **Queries:** Usadas para ler dados (equivalente ao `GET` em REST).
* **Mutations:** Usadas para criar, alterar ou deletar dados (equivalente a `POST`, `PUT`, `DELETE`).
* **Subscriptions:** Usadas para conexões em tempo real via WebSockets.

### 3. Requisição e Resposta

O cliente faz uma **Query**:

```graphql
query {
  obterUsuario(id: "1") {
    nome
    posts {
      titulo
    }
  }
}

```

E o servidor responde exatamente com essa estrutura:

```json
{
  "data": {
    "obterUsuario": {
      "nome": "Ana",
      "posts": [
        { "titulo": "Introdução ao GraphQL" }
      ]
    }
  }
}

```

---

## 🟢 Vantagens e 🔴 Desvantagens

### Vantagens

* **Performance em Redes Móveis:** Reduz a quantidade de bytes trafegados.
* **Tipagem Forte:** Autocompletar e validação imediata em ferramentas de desenvolvimento.
* **Evolução da API:** É possível adicionar novos campos sem quebrar versões antigas do aplicativo (facilita descontinuar campos antigos com a diretiva `@deprecated`).

### Desvantagens

* **Complexidade no Backend:** Resolver consultas aninhadas pode gerar o problema de performance conhecido como **N+1 Queries** (que exige soluções como DataLoader).
* **Caching HTTP:** Em REST, você usa o cache padrão do navegador baseado em URLs/métodos HTTP. No GraphQL, o cache precisa ser gerenciado no cliente (como Apollo Client ou Relay).
* **Ataques de Negação de Serviço (DoS):** Se não houver proteção, consultas excessivamente profundas/aninhadas podem derrubar o servidor.