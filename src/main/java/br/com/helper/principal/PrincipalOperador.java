package br.com.helper.principal;

import java.util.Scanner;

import br.com.helper.operadores.OperadorIncremento;

public class PrincipalOperador {

	public static void main(String[] args) {
		System.out.println("OPERADORES");
		OperadorIncremento oi = new OperadorIncremento();

		System.out.println("1. TESTE COM OPERADOR DE INCREMENTO");

		try (Scanner ler = new Scanner(System.in)) {
			int key = ler.nextInt();
			switch (key) {
			case 1:
				oi.incremento();

			default:
				throw new IllegalArgumentException("Unexpected value: " + key);
			}
		}

	}

}
