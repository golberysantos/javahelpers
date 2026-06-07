package br.com.helper.principal;

import java.util.Scanner;

import br.com.helper.oop.contabancaria.ContaBancaria;

public class MenuContaBancaria {
	
	public void exibir() {
	
	try (Scanner scanner = new Scanner(System.in)) {
		int saldoInicial = scanner.nextInt();
		scanner.nextLine(); // consume newline

		ContaBancaria conta = new ContaBancaria(saldoInicial);

		while (true) {
			String linha = scanner.nextLine().trim();
			if (linha.equals("fim")) {
				break;
			}

			String[] partes = linha.split(" ");
			String operacao = partes[0];
			int valor = Integer.parseInt(partes[1]);

			if (operacao.equals("depositar")) {
				conta.depositar(valor);
			} else if (operacao.equals("sacar")) {
				conta.sacar(valor);
			}
		}

		System.out.println(conta.getSaldo());
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
