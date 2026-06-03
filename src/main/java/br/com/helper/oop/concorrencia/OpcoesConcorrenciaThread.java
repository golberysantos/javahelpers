package br.com.helper.oop.concorrencia;

import java.util.Scanner;

public class OpcoesConcorrenciaThread {

	public void show() {
		System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
		System.out.println("║ Lelpj - Lista de Exercícios de Linguagem de Programação Java. By Golbery ║");
		System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");
		System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
		System.out.println("║ Escolha uma oopção:                                                      ║");
		System.out.println("║ 1 - Exemplo 01 My Thread Com Extends                                     ║");
		System.out.println("║ 2 -                                                          ║");
		System.out.println("║ 3 -                                                          ║");
		System.out.println("║ 4 - Exercício 4.                                                         ║");
		System.out.println("║ 5 - Exercício 5.                                                         ║");
		System.out.println("║ 0 - Sair                                                                 ║");
		System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");

		ConcorrenciaThreadController ctc;
		try (Scanner l = new Scanner(System.in)) {
			short key = l.nextShort();
			switch (key) {
			case 1 -> new ConcorrenciaThreadController().mostrarExemplo01ComExtends();
			default -> throw new IllegalArgumentException("Unexpected value: " + key);
			}
		}
	}
}
