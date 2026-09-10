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
	 */
	static void prova02(){
		int[] nums = new int[2];
		nums[0] = 5;
		System.out.println(nums[0] + nums[1]);
	}
	
	/***
	 * 3. O que acontece?	  
	 */
	static void prova03(){
		String[] names = new String[3];
		System.out.println(names[1].length());
	}
	
	/***
	 * 4. Compila?	  
	 */
	static void prova04(){
		int[] arr1 = {1, 2, 3};
		int[] arr2 = new int[]{4, 5, 6};
		arr1 = arr2;
		arr2 = {7, 8, 9};  // Linha X
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
	}

	public static void main(String[] args) {
		int[] valores = { 5, 2, 8, 1, 9 };
		System.out.println("Máximo: " + findMax(valores));
		System.out.println("Conteúdo do array: " + Arrays.toString(valores));
		prova05();
	}
}
