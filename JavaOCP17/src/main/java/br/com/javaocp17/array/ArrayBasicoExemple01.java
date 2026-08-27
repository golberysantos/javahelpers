package br.com.javaocp17.array;


public class ArrayBasicoExemple01 {
    public static void main(String[] args) {
        // Declaração e inicialização
        int[] numeros = new int[] {};
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
        
        System.out.println(  findMax(new int[]{})   );
    }
    
    public static int findMax(int[] numbers) {
    	System.out.println("\n\n"+ numbers);
    	int max = Integer.MIN_VALUE;
    	try {
			if (numbers == null || numbers.length == 0) {
				throw new IllegalArgumentException("Array não pode ser nulo ou vazio");
			}
			max = numbers[0];
			for (int i = 1; i < numbers.length; i++) {
				if (numbers[i] > max) {
					max = numbers[i];
				}
			}
			
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
    	return max;
		
	}
}
