# O que são "casos de borda" (edge cases)?

**Casos de borda** (do inglês *edge cases*) são **situações extremas, limites ou incomuns** que um programa pode enfrentar — justamente aquelas que costumam ser esquecidas e onde os bugs mais aparecem.

A ideia: quando você escreve um código, normalmente pensa no **caso comum** ("caminho feliz"). Mas o programa também precisa funcionar nos **extremos** — nos limites do que é possível.

---

## 🧠 Por que "borda"?

Imagine um intervalo de valores válidos, tipo uma **caixa**:

```
        valores válidos
   ┌─────────────────────────┐
   │                         │
   │    1   2   3   4   5    │
   │                         │
   └─────────────────────────┘
     ↑                       ↑
   borda                   borda
  (mínimo)                (máximo)
```

As **bordas** são os extremos: o menor valor, o maior valor, o vazio, o zero, o `null`... Os problemas geralmente moram exatamente aí, não no meio.

---

## 📋 Exemplos clássicos de casos de borda

### 1. Listas e arrays
```java
int[] v = {1, 2, 3};

// Caso comum
v[1];        // ✅ OK

// Casos de borda
v[0];        // primeiro elemento
v[v.length - 1];  // último elemento
v[v.length];      // ❌ ArrayIndexOutOfBoundsException
new int[0];       // array vazio
null              // array nulo
```

### 2. Números
- **Zero** (`0`)
- **Negativos** (`-1`)
- **Valor máximo/mínimo** de um tipo (`Integer.MAX_VALUE`, `Integer.MIN_VALUE`)
- **Divisão por zero**
- **Estouro de inteiro** (*overflow*):
  ```java
  int x = Integer.MAX_VALUE;
  x = x + 1;   // vira Integer.MIN_VALUE! (overflow)
  ```

### 3. Strings
```java
String s = "";
s.length();          // 0 — string vazia
s.charAt(0);         // ❌ StringIndexOutOfBoundsException
s = null;
s.length();          // ❌ NullPointerException
```

### 4. Loops
```java
for (int i = 0; i < n; i++) { ... }

// Casos de borda:
n = 0;    // loop não executa nenhuma vez
n = 1;    // executa uma vez
n < 0;    // pode causar comportamento estranho
```

### 5. Entrada do usuário
- Campo vazio
- Espaços em branco
- Caracteres especiais (`@`, `#`, emojis)
- Texto onde se esperava número
- Texto muito longo

---

## 🎯 Exemplo prático

Função que calcula a média de um array:

```java
double media(int[] v) {
    int soma = 0;
    for (int x : v) soma += x;
    return soma / v.length;   // ⚠️ cuidado!
}
```

**Casos de borda a considerar:**
| Entrada | Problema |
|---|---|
| `v = {}` (vazio) | divisão por zero → `v.length == 0` |
| `v = null` | `NullPointerException` |
| `v = {Integer.MAX_VALUE, Integer.MAX_VALUE}` | overflow na soma |
| `v = {1}` | único elemento (funciona, mas vale testar) |

Versão mais robusta:
```java
double media(int[] v) {
    if (v == null || v.length == 0) {
        throw new IllegalArgumentException("Array vazio ou nulo");
    }
    long soma = 0;                 // long evita overflow
    for (int x : v) soma += x;
    return (double) soma / v.length;
}
```

---

## 🧪 Casos de borda vs Casos de teste

- **Caso comum (happy path):** entrada típica, tudo funciona.
- **Caso de borda (edge case):** entrada no limite — vazio, zero, máximo, mínimo, `null`.
- **Caso extremo (corner case):** combinação de vários casos de borda ao mesmo tempo (ex.: array vazio **e** nulo, ou duas condições extremas juntas).

> Muitas vezes "edge case" e "corner case" são usados como sinônimos, mas *corner case* costuma ser uma situação ainda mais rara e específica.

---

## 💡 Por que isso importa?

1. **Bugs se escondem nas bordas.** O caminho do meio geralmente funciona; o problema está no extremo.
2. **Segurança.** Entradas maliciosas exploram casos de borda (ex.: buffer overflow).
3. **Qualidade.** Um bom programador testa os limites, não só o caso feliz.
4. **Testes.** Ao escrever testes, sempre inclua: mínimo, máximo, vazio, nulo, um elemento, muitos elementos.

---

## 🧠 Resumo em uma frase

> **Casos de borda são as situações-limite (vazio, zero, máximo, mínimo, nulo) onde o comportamento do programa costuma quebrar — e por isso precisam ser pensados e testados de propósito.**

Uma boa regra prática: sempre que você escreve um loop, uma divisão, um acesso a índice ou lê uma entrada, **pergunte-se: "e se for zero? e se for vazio? e se for nulo? e se for o máximo?"**