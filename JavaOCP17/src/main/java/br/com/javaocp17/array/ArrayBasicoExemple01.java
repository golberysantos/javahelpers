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

	public static void main(String[] args) {
		int[] valores = { 5, 2, 8, 1, 9 };
		System.out.println("Máximo: " + findMax(valores));
		System.out.println("Conteúdo do array: " + Arrays.toString(valores));
	}
}
