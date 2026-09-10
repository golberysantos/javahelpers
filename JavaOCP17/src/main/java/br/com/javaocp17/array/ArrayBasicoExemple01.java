package br.com.javaocp17.array;

import java.util.Arrays;

public class ArrayBasicoExemple01 {
	public static int findMax(int[] numbers) {
		if (numbers == null) {
			throw new IllegalArgumentException("Array não pode ser nulo");
		}
		if (numbers.length == 0) {
			throw new IllegalArgumentException("Array não pode ser vazio");
		}

		int max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (numbers[i] > max) {
				max = numbers[i];
			}
		}
		return max;
	}

	/***
	 * 1. O código abaixo compila? Se não, por quê?
	 */
	private static void prova01() {
		int[] a = new int[0];
		a[0] = 10;
		System.out.println(a[0]);
		/*
		 * O código não compila, pois o array 'a' é inicializado com tamanho 0, o que
		 * significa que não há elementos no array. Portanto, tentar acessar a posição
		 * 0 (a[0]) resulta em um erro de índice fora dos limites (ArrayIndexOutOfBoundsException).
		 *     Regra: Array de tamanho 0 é válido (é um objeto), mas qualquer acesso é inválido. Índices válidos: nenhum.
Pegadinha OCP: Muitos acham que new int[0] é erro de compilação. Não é! É um array vazio válido. Só não pode acessar nenhum índice.
		 */
	}

	/***
	 * 2. Qual é a saída?	  
	 * 
	 * */
	static void prova02(){
		int[] nums = new int[2];
		nums[0] = 5;
		System.out.println(nums[0] + nums[1]);
		String txt = """
				A saída será 5. O array 'nums' é inicializado com tamanho 2, então ele possui
		dois elementos: nums[0] e nums[1]. O elemento nums[0] é atribuído o valor 5,
		enquanto nums[1] não é explicitamente inicializado, então ele recebe o valor
		padrão para inteiros, que é 0. Portanto, a expressão nums[0] + nums[1] resulta
		em 5 + 0 = 5.
		  
		Regra: Arrays de primitivos são inicializados com valores padrão:

				int, short, byte, long → 0

				float, double → 0.0

				boolean → false

				char → '\u0000' (null character)

		Pegadinha OCP: A saída não é 5 0 (dois valores), é 5 (soma). Muitos leem rápido e erram.
				""";
		System.out.println(txt);
		
	}
	
	/***
	 * 3. O que acontece?	  
	 */
	static void prova03(){
		String[] names = new String[3];
		
		System.out.println("""
				Regra: Arrays de objetos são inicializados com null em todas as posições. Chamar método em null → NullPointerException.
				Pegadinha OCP: O compilador não detecta isso. É um erro em runtime, não em compilação. A OCP adora esse tipo de questão.
				""");
		System.out.println(names[1].length());
	}
	
	/***
	 * 4. Compila?	  
	 */
	static void prova04(){
		int[] arr1 = {1, 2, 3};
		int[] arr2 = new int[]{4, 5, 6};
		arr1 = arr2;
		//arr2 = {7, 8, 9};  // errado.
		arr2 = new int[]{7, 8, 9};  // ✅ Válido
		
		System.out.println("""				
				Explicação.
				- Compilação: ❌ Erro na linha X
				- Erro: Array constants can only be used in initializers
				- Regra: A sintaxe {...} só é válida na declaração da variável. Em atribuições posteriores, use new int[]{...}.
				""");
	}
	
	/***
	 * 5. Qual é a saída?  
	 */
	static void prova05(){
		int[] x = new int[3];
		int[] y = x;
		x[0] = 10;
		y[1] = 20;
		System.out.println(x[0] + " " + x[1]);
		
		System.out.println("""
				Explicação.
				- Saída: 10 20
				- Regra: Arrays são objetos. int[] y = x; não copia o array. Copia a referência. x e y apontam para o mesmo objeto na memória.
				Atribuição de arrays copia a referência, não os valores. Portanto, x e y referenciam o mesmo array.
				Pegadinha OCP: Muitos acham que int[] y = x cria uma cópia. Não cria! Para copiar, use:
					int[] y = Arrays.copyOf(x, x.length);
					// ou
					int[] y = x.clone();
				""");
	}
	
	//	🎯 Próximo tópico: Arrays de Objetos e Arrays Multidimensionais 
	
	static void prova06(){
		String[] nomes = new String[3];
		nomes[0] = "Ana";
		nomes[1] = "João";
		System.out.println(nomes[2]);
	}

	public static void main(String[] args) {
		int[] valores = { 5, 2, 8, 1, 9 };
		System.out.println("Máximo: " + findMax(valores));
		System.out.println("Conteúdo do array: " + Arrays.toString(valores));
		prova06();
	}
}
