
## 📝 Perguntas para você (Modo Prova):

**1.** O código abaixo compila? Se não, por quê?

```java
int[] a = new int[0];
a[0] = 10;
System.out.println(a[0]);
```

**2.** Qual é a saída?

```java
int[] nums = new int[2];
nums[0] = 5;
System.out.println(nums[0] + nums[1]);
```

**3.** O que acontece?

```java
String[] names = new String[3];
System.out.println(names[1].length());
```

**4.** Compila?

```java
int[] arr1 = {1, 2, 3};
int[] arr2 = new int[]{4, 5, 6};
arr1 = arr2;
arr2 = {7, 8, 9};  // Linha X
```

**5.** Qual é a saída?

```java
int[] x = new int[3];
int[] y = x;
x[0] = 10;
y[1] = 20;
System.out.println(x[0] + " " + x[1]);
```

---
Excelente! Vamos analisar cada resposta com rigor:

---

## 📊 Análise das Questões — Modo Prova

### Questão 1
```java
int[] a = new int[0];
a[0] = 10;
System.out.println(a[0]);
```

**Sua resposta:** "Não funciona. há exception: Index 0 out of bounds for length 0."

**✅ Correto!**

**Análise:**
- **Compilação:** ✅ Compila normalmente
- **Runtime:** ❌ `ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0`
- **Regra:** Array de tamanho 0 é válido (é um objeto), mas qualquer acesso é inválido. Índices válidos: **nenhum**.

**Pegadinha OCP:** Muitos acham que `new int[0]` é erro de compilação. Não é! É um array vazio válido. Só não pode acessar nenhum índice.

---

### Questão 2
```java
int[] nums = new int[2];
nums[0] = 5;
System.out.println(nums[0] + nums[1]);
```

**Sua resposta:** "Funciona."

**✅ Correto!**

**Análise:**
- **Compilação:** ✅
- **Runtime:** ✅
- **Saída:** `5` (porque `nums[1]` tem valor padrão `0`)

**Regra:** Arrays de primitivos são inicializados com valores padrão:
- `int`, `short`, `byte`, `long` → `0`
- `float`, `double` → `0.0`
- `boolean` → `false`
- `char` → `'\u0000'` (null character)

**Pegadinha OCP:** A saída não é `5 0` (dois valores), é `5` (soma). Muitos leem rápido e erram.

---

### Questão 3
```java
String[] names = new String[3];
System.out.println(names[1].length());
```

**Sua resposta:** "não funciona. Exception in thread... Cannot invoke "String.length()" because "names[1]" is null."

**✅ Correto!**

**Análise:**
- **Compilação:** ✅ (o compilador não sabe que `names[1]` é null)
- **Runtime:** ❌ `NullPointerException`
- **Saída:** Nenhuma (exceção antes do println)

**Regra:** Arrays de objetos são inicializados com `null` em todas as posições. Chamar método em `null` → `NullPointerException`.

**Pegadinha OCP:** O compilador não detecta isso. É um erro **em runtime**, não em compilação. A OCP adora esse tipo de questão.

---

### Questão 4
```java
int[] arr1 = {1, 2, 3};
int[] arr2 = new int[]{4, 5, 6};
arr1 = arr2;
arr2 = {7, 8, 9};  // Linha X
```

**Sua resposta:** "não funciona. Array constants can only be used in initializers."

**✅ Correto!**

**Análise:**
- **Compilação:** ❌ Erro na linha X
- **Erro:** `Array constants can only be used in initializers`
- **Regra:** A sintaxe `{...}` só é válida na **declaração** da variável. Em atribuições posteriores, use `new int[]{...}`.

**Correção:**
```java
arr2 = new int[]{7, 8, 9};  // ✅ Válido
```

**Pegadinha OCP:** Essa é uma das pegadinhas mais clássicas. A OCP coloca `{...}` em uma atribuição no meio do código para ver se você percebe.

---

### Questão 5
```java
int[] x = new int[3];
int[] y = x;
x[0] = 10;
y[1] = 20;
System.out.println(x[0] + " " + x[1]);
```

**Sua resposta:** "funciona. imprime 10 20."

**✅ Correto!**

**Análise:**
- **Compilação:** ✅
- **Runtime:** ✅
- **Saída:** `10 20`

**Regra:** `int[] y = x;` **não copia** o array. Copia a **referência**. `x` e `y` apontam para o **mesmo objeto** na memória.

```
Stack          Heap
------         ------
x  ────────→  [0, 0, 0]  ←────── y
              ↑
              x[0] = 10 → [10, 0, 0]
              y[1] = 20 → [10, 20, 0]
```

**Pegadinha OCP:** Muitos acham que `int[] y = x` cria uma cópia. Não cria! Para copiar, use:
```java
int[] y = Arrays.copyOf(x, x.length);
// ou
int[] y = x.clone();
```

---

# Diferença entre as duas formas de inicializar arrays em Java

## `int[] numeros = {1, 2, 3}` vs `int[] numeros = new int[]{1, 2, 3}`

**Funcionalmente, são idênticas.** As duas criam um array de 3 inteiros com os mesmos valores. A diferença é **sintática/contextual**:

### 1. `int[] numeros = {1, 2, 3};`
- É um **atalho (shortcut)** permitido apenas **na declaração da variável**.
- O compilador infere o tipo e o tamanho a partir dos elementos.
- **Não pode** ser usado em outros contextos, como:
  ```java
  numeros = {1, 2, 3};        // ❌ Erro de compilação
  metodo({1, 2, 3});          // ❌ Erro de compilação
  return {1, 2, 3};           // ❌ Erro de compilação
  ```

### 2. `int[] numeros = new int[]{1, 2, 3};`
- É a forma **explícita e completa**.
- Pode ser usado em **qualquer contexto**, inclusive fora de declarações:
  ```java
  numeros = new int[]{1, 2, 3};        // ✅ OK
  metodo(new int[]{1, 2, 3});          // ✅ OK
  return new int[]{1, 2, 3};           // ✅ OK
  ```

### Resumo
| Situação | `{...}` | `new int[]{...}` |
|---|---|---|
| Na declaração | ✅ | ✅ |
| Fora da declaração | ❌ | ✅ |

> 💡 Dica: `{...}` é chamado de *array initializer*. Só funciona quando o compilador já sabe o tipo pelo contexto da declaração.

---

# Erro de Compilação vs Erro de Runtime

## 🔴 Erro de Compilação (Compile-time error)
Ocorre **antes do programa executar**, durante a compilação (javac). O código nem chega a virar `.class`.

**Características:**
- Detectado pelo compilador.
- Impede a geração do bytecode.
- Geralmente é erro de **sintaxe** ou **tipo**.

**Exemplos:**
```java
int x = "texto";              // tipo incompatível
System.out.println(y);        // variável não declarada
if (x > 0) {                  // falta fechar chave
numeros = {1, 2, 3};          // inicialização inválida fora da declaração
metodo(1, 2);                 // método não existe
```

## 🟠 Erro de Runtime (Runtime error / Exception)
Ocorre **durante a execução** do programa. O código compilou e começou a rodar, mas algo deu errado em tempo de execução.

**Características:**
- Detectado pela JVM em execução.
- Pode ser tratado com `try/catch`.
- Muitas vezes depende dos dados de entrada.

**Exemplos:**
```java
int[] v = new int[3];
System.out.println(v[5]);        // ArrayIndexOutOfBoundsException

String s = null;
System.out.println(s.length());  // NullPointerException

int a = 10, b = 0;
System.out.println(a / b);       // ArithmeticException (divisão por zero)
```

## Comparação rápida

| Aspecto | Compilação | Runtime |
|---|---|---|
| Quando ocorre | Antes de executar | Durante a execução |
| Quem detecta | Compilador (javac) | JVM |
| Pode tratar com try/catch? | ❌ | ✅ |
| Exemplo típico | `int x = "abc";` | `v[10]` fora do limite |
| Programa chega a rodar? | ❌ | ✅ (até o ponto do erro) |

> ⚠️ Observação: em Java, mesmo erros de runtime são "compiláveis" porque são **exceptions** — o compilador só exige que você trate as *checked exceptions* (ex.: `IOException`). As *unchecked* (`NullPointerException`, `ArrayIndexOutOfBoundsException`) não precisam ser declaradas, mas podem estourar em execução.

---

## 🎯 Próximo tópico: Arrays de Objetos e Arrays Multidimensionais

Antes de avançar, quero que você reflita sobre:

**1.** O que acontece neste código?
```java
String[] nomes = new String[3];
nomes[0] = "Ana";
nomes[1] = "João";
System.out.println(nomes[2]);
```

**2.** O que acontece neste código?
```java
String[] nomes = new String[3];
System.out.println(nomes[0].toUpperCase());
```

**3.** O que acontece neste código?
```java
int[][] matriz = new int[2][3];
System.out.println(matriz.length);
System.out.println(matriz[0].length);
```

**4.** O que acontece neste código?
```java
int[][] matriz = new int[2][];
System.out.println(matriz[0]);
```

**Preveja as saídas e depois me diga: quer que eu explique arrays de objetos e multidimensionais agora, ou prefere fazer mais exercícios de arrays unidimensionais primeiro?**

---

## 📝 Checklist parcial — Arrays (subtema 1)

```text
[x] Consigo explicar o conceito de array
[x] Consigo escrever código com arrays
[x] Consigo identificar erros de compilação
[x] Consigo prever resultados
[x] Consigo identificar exceções
[x] Consigo explicar o comportamento
[ ] Consigo resolver exercícios (precisa corrigir o findMax)
[ ] Consigo resolver questões OCP (5/5 no modo prova)
[ ] Consigo identificar pegadinhas
[ ] Consigo aplicar o conceito em código real
```

**Antes de avançar**, precisamos corrigir o exercício `findMax`. Você entendeu por que capturar a exceção era errado? Consegue reescrever o método agora com a correção?

**Sua vez!** 🚀