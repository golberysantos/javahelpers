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

Quando tiver sua solução (ou depois de tentar bastante), me envie e farei o Code Review completo, analisando compilação, lógica, casos de borda, complexidade e possíveis pegadinhas OCP.

O que você acha? Vamos começar com a pergunta do Modo Fail Fast (código do ArrayPegadinha) e depois o exercício.