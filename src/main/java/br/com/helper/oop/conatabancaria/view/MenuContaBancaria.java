package br.com.helper.oop.conatabancaria.view;

import java.math.BigDecimal;
import java.util.Scanner;

import br.com.helper.oop.contabancaria.ContaBancaria;

public class MenuContaBancaria {

	public void exibir() {

		System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
		System.out.println("║ CONTA BANCÁRIA COM POO Java. By Golbery                                  ║");
		System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");
		System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
		System.out.println("║ Escolha uma oopção:                                                      ║");
		System.out.println("║ 0 - Sair                                                                 ║");
		System.out.println("║ 1 - Depositar                                                            ║");
		System.out.println("║ 2 - Sacar                                                                ║");
		System.out.println("║ 3 -                                                          ║");
		System.out.println("║ 4 -                                                          ║");
		System.out.println("║ 5 -                                                          ║");
		try (Scanner scanner = new Scanner(System.in)) {

			BigDecimal saldoInicial = new BigDecimal("1000");


			ContaBancaria conta = new ContaBancaria(saldoInicial);

			while (true) {
				System.out.println("INFORME A OPÇÃO");
				String linha = scanner.nextLine().trim();
				if (linha.equals("0")) {
					break;
				}


				switch (linha) {
				case "1" -> conta.depositar(scanner.nextBigDecimal());
				case "2" -> conta.sacar(scanner.nextBigDecimal());
				default ->
				throw new IllegalArgumentException();
				}
			}

			System.out.println(conta.getSaldo());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
