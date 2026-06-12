package br.com.helper.exceptions;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ExemploRegraExcecao {
	public void exibir() {
		System.out.println("==================================================");
		System.out.println("1. UNCHECKED EXCEPTION (Não Checada)");
		System.out.println("==================================================");

		// O código abaixo compila perfeitamente sem erros de compilação,
		// embora vá quebrar quando rodar (ArrayIndexOutOfBoundsException).
		// O compilador NÃO te obriga a usar try-catch aqui.
		int[] numeros = { 1, 2, 3 };
		System.out.println("Acessando posição inexistente: " + numeros[10]);

		
		System.out.println("\n==================================================");
		System.out.println("2. CHECKED EXCEPTION (Checada)");
		System.out.println("==================================================");
		// O compilador do Java SABE que a classe FileReader pode lançar uma
		// Checked Exception chamada FileNotFoundException.
		// Por isso, ele nos obriga a colocar essa linha dentro de um try-catch
		// ou declarar o 'throws' no método.

		/*
		 * Se você descomentar a linha abaixo, o código NÃO COMPILARÁ: 
		 */
		//FileReader leitor = new FileReader("arquivo_inexistente.txt");

		// Forma correta exigida pelo compilador (com tratamento):
		try {
			FileReader leitor = new FileReader("arquivo_inexistente.txt");
			System.out.println("Arquivo aberto com sucesso!");
		} catch (FileNotFoundException e) {
			System.out.println("Erro compilado e tratado: Arquivo não foi encontrado!");
		}
	}
}
