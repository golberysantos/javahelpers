🎯 Aula 1: Declaração e Inicialização de Arrays
1. Introdução teórica

Definição: Um array em Java é um objeto que contém um número fixo de elementos do mesmo tipo. O tipo pode ser primitivo (int[], double[]) ou referência (String[], Object[]).

Comportamento: Arrays são indexados por inteiros começando em 0. O atributo length indica o número total de elementos. O tamanho é definido na criação e não pode ser alterado.

Diferenças importantes:

    Arrays de primitivos armazenam valores diretamente no array

    Arrays de objetos armazenam referências para os objetos

    int[] é diferente de Integer[] (o primeiro tem valores, o segundo tem referências para Integer)

2. Regras da Linguagem
Declaração:
java

int[] numeros;      // Forma preferida (tipo[] nome)
int numeros[];      // Válida, mas não recomendada
int[] numeros2[];   // Isso é um array bidimensional! (confuso)

Inicialização:
java

int[] numeros = new int[3];        // 3 elementos, valor padrão 0
int[] numeros = {1, 2, 3};         // Inicialização com valores (apenas na declaração)
int[] numeros = new int[]{1, 2, 3}; // Array inicializer anônimo

O que Java determina sobre arrays:

    Regra da linguagem: Arrays são objetos, mesmo quando o tipo é primitivo

    Compilação: Verifica se o tipo do elemento é compatível

    Runtime: Índices são verificados (ArrayIndexOutOfBoundsException)

    Boa prática: Use int[] numeros em vez de int numeros[]

    Particularidade OCP: Declarações confusas como int[] numeros[] podem aparecer

3. Exemplo prático

Vamos começar com um exemplo completo e executável:
java

package arrays;

public class ArrayBasico {
    public static void main(String[] args) {
        // Declaração e inicialização
        int[] numeros = new int[3];
        String[] nomes = new String[2];
        
        // Atribuindo valores
        numeros[0] = 10;
        numeros[1] = 20;
        numeros[2] = 30;
        
        nomes[0] = "Ana";
        nomes[1] = "João";
        
        // Acessando e imprimindo
        System.out.println("Números:");
        for (int i = 0; i < numeros.length; i++) {
            System.out.println("numeros[" + i + "] = " + numeros[i]);
        }
        
        System.out.println("\nNomes:");
        for (int i = 0; i < nomes.length; i++) {
            System.out.println("nomes[" + i + "] = " + nomes[i]);
        }
        
        // Valores padrão
        System.out.println("\nValor padrão de int: " + new int[1][0]);
        System.out.println("Valor padrão de String: " + new String[1][0]);
    }
}

Saída esperada:
text

Números:
numeros[0] = 10
numeros[1] = 20
numeros[2] = 30

Nomes:
nomes[0] = Ana
nomes[1] = João

Valor padrão de int: 0
Valor padrão de String: null

4. Modo Fail Fast

Agora, analise este código:
java

public class ArrayPegadinha {
    public static void main(String[] args) {
        int[] a = new int[2];
        int b[] = new int[2];
        int[] c = new int[]{1, 2, 3};
        int[] d = {1, 2, 3};
        int[] e = new int[3]{1, 2, 3};  // LINHA PROBLEMÁTICA
    }
}

Pergunta para você prever:

O código compila? Se não, qual linha tem problema e por quê? Se compila, qual é a saída?

Pense antes de responder! 😉
5. Experimento Hands-on

Após você responder (e tentar mentalmente ou no Eclipse), vamos fazer alguns experimentos:

Experimento 1: O que acontece se no código original do ArrayBasico você tentar acessar numeros[3] (índice 3) sendo que o tamanho é 3?

Experimento 2: O que acontece se você declarar int[] numeros = new int[-1]?

Experimento 3: O que acontece se você fizer numeros = {10, 20, 30} fora da declaração (re-atribuindo)?

Preveja cada caso antes de testar!
6. Exercício guiado

Contexto: Você precisa implementar um método que encontra o maior valor em um array de inteiros.

Objetivo: Escrever um método que retorne o maior valor de um array, ou lance uma exceção se o array for nulo ou vazio.

Requisitos:

    Método: public static int findMax(int[] numbers)

    Retorna o maior valor

    Lança IllegalArgumentException se numbers == null || numbers.length == 0

    Não modifica o array original

Código inicial:
java

public class ArrayExercicio {
    public static int findMax(int[] numbers) {
        // Implemente aqui
    }
    
    public static void main(String[] args) {
        int[] valores = {5, 2, 8, 1, 9};
        System.out.println("Máximo: " + findMax(valores)); // Deve imprimir 9
    }
}

Casos de teste:

    findMax(new int[]{5, 2, 8, 1, 9}) → 9

    findMax(new int[]{-5, -2, -8, -1, -9}) → -1

    findMax(new int[]{7}) → 7

    findMax(null) → IllegalArgumentException

    findMax(new int[]{}) → IllegalArgumentException

Tente resolver antes de olhar qualquer dica!
Dicas (use só se precisar):

Dica 1: Pense em como você encontraria o maior valor manualmente, olhando elemento por elemento.

Dica 2: Comece com o primeiro valor como o maior, depois compare com os demais. Lembre-se dos casos de borda (null, vazio, único elemento).

Dica 3: Não precisa ordenar o array. Uma única passagem é suficiente.

Dica 4: Pseudo-código:
text

se numbers for null ou vazio:
    lançar exceção
    
maior = numbers[0]
para cada número em numbers[1..n-1]:
    se número > maior:
        maior = número
retornar maior


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


## 🧠 Entendendo Arrays Multidimensionais em Profundidade

### O que é `int[][]`?

**Definição:** Em Java, **não existem arrays multidimensionais verdadeiros**. O que existe são **arrays de arrays**.

```java
int[][] matriz = new int[2][3];
```

Isso **não** é uma matriz 2x3 na memória. Isso é:

1. Um array de tamanho 2 (`matriz`)
2. Cada elemento de `matriz` é uma referência para um `int[]`
3. Cada `int[]` tem tamanho 3

### Representação na memória:

```
Stack          Heap
------         ------
matriz  ────→  [ ref0, ref1 ]    ← array de 2 elementos (matriz.length = 2)
                 │      │
                 │      └──→ [0, 0, 0]  ← array de 3 elementos (matriz[1].length = 3)
                 │
                 └──────────→ [0, 0, 0]  ← array de 3 elementos (matriz[0].length = 3)
```

No código `int[][] m = new int[2][3];`, o primeiro colchete representa a **linha** e o segundo representa a **coluna**.

* **`2`** é o número de **linhas** (`i`).
* **`3`** é o número de **colunas** (`j`).

Ao acessar um elemento específico usando `m[i][j]`:

* **`i`** representa o índice da **linha** (variando de `0` a `1`).
* **`j`** representa o índice da **coluna** (variando de `0` a `2`).

### Comparando os dois casos:

**Caso A: `new int[2][3]`**
```java
int[][] matriz = new int[2][3];
```
- `matriz` → array de 2 referências
- Cada referência aponta para um `int[]` de tamanho 3
- `matriz[0]` → `[0, 0, 0]` (array válido)
- `matriz[0][0]` → `0` (valor padrão)

**Caso B: `new int[2][]`**
```java
int[][] matriz = new int[2][];
```
- `matriz` → array de 2 referências
- Cada referência é `null` (não foi inicializada)
- `matriz[0]` → `null`
- `matriz[0][0]` → ❌ `NullPointerException`

---

## 💡 Por que isso importa para a OCP?

A OCP cobra **exatamente** essa distinção. Exemplo clássico:

```java
int[][] matriz = new int[3][];
matriz[0] = new int[2];
matriz[1] = new int[5];
matriz[2] = new int[3];

System.out.println(matriz.length);      // ?
System.out.println(matriz[0].length);   // ?
System.out.println(matriz[1].length);   // ?
System.out.println(matriz[2].length);   // ?
```

**Saída:**
```
3
2
5
3
```

**Regra:** Arrays multidimensionais em Java podem ser **irregulares** (jagged arrays). Cada "linha" pode ter um tamanho diferente.

**Isso é uma pegadinha OCP clássica:**
```java
int[][] matriz = new int[2][3];
matriz[0] = new int[5];  // Válido! Agora matriz[0] tem 5 elementos
System.out.println(matriz[0].length);  // 5
System.out.println(matriz[1].length);  // 3
```

---

## 📝 Exercício rápido (responda antes de continuar):

**1.** O que acontece?
```java
int[][] m = new int[2][];
System.out.println(m[0].length);
```

**2.** O que acontece?
```java
int[][] m = new int[2][3];
System.out.println(m[1][2]);
```

**3.** O que acontece?
```java
int[][] m = new int[2][3];
m[0] = new int[5];
System.out.println(m[0].length + " " + m[1].length);
```

**4.** O que acontece?
```java
int[][] m = {{1, 2}, {3, 4, 5}, {6}};
System.out.println(m.length);
System.out.println(m[1].length);
System.out.println(m[2][0]);
```

**5.** O que acontece?
```java
int[][] m = new int[3][];
m[0] = new int[]{1, 2};
m[1] = new int[]{3, 4, 5};
m[2] = new int[]{6};
System.out.println(m[1][1]);
```

**Responda essas 5 questões. Depois faremos o Code Review do seu `findMax` corrigido e avançaremos para varargs e exercícios mais complexos.** 🚀

