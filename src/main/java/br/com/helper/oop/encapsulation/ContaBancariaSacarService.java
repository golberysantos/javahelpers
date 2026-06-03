package br.com.helper.oop.encapsulation;

import java.util.Scanner;

public class ContaBancariaSacarService {
	public void sacar() {

		try (Scanner scanner = new Scanner(System.in)) {
			try {
				String saldoInput = scanner.nextLine();
				String saqueInput = scanner.nextLine();

				int saldo = Integer.parseInt(saldoInput.trim());
				int valorSaque = Integer.parseInt(saqueInput.trim());

				if (valorSaque <= 0) {
					System.out.println("Valor invalido");
					return;
				}

				if (valorSaque > saldo) {
					System.out.println("Saldo insuficiente");
					return;
				}

				System.out.println(saldo - valorSaque);

			} catch (NumberFormatException e) {
				System.out.println("Entrada invalida");
			}
		}

	}
}
