package br.com.javaocp17.array;


public class ArrayBasicoExemple01 {
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
        System.out.println(numeros[3]);
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
