package br.com.helper.util;

import java.util.Scanner;

public class ValidaValorMonetario {

	public static void main(String[] args) {
		double saldo = 0;

		Scanner scanner = new Scanner(System.in);
		System.out.println("Por favor, digite double:");
		saldo = lerSaldo(scanner);
		System.out.println(saldo);

	}

	private static double lerSaldo(Scanner scanner) {
		while (true) {
			try {
				String entrada = scanner.nextLine().trim();
				entrada = entrada.replace(",", ".");
				return Double.parseDouble(entrada);
			} catch (NumberFormatException e) {
				System.out.println("Valor inválido. Digite o saldo novamente:");
			}
		}
	}

}
