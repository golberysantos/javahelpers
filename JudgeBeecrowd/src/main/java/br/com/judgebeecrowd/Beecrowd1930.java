package br.com.judgebeecrowd;

import java.io.IOException;
import java.util.Scanner;

/**
 * IMPORTANT: O nome da classe deve ser "Main" para que a sua solução execute
 * Class name must be "Main" for your solution to execute El nombre de la clase
 * debe ser "Main" para que su solución ejecutar
 */
public class Beecrowd1930 {

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);

		// Lendo os 4 números inteiros
		int T1 = scanner.nextInt();
		int T2 = scanner.nextInt();
		int T3 = scanner.nextInt();
		int T4 = scanner.nextInt();

		// Calculando o número máximo de aparelhos
		// Soma todas as tomadas e subtrai 3 (conexões entre as 4 réguas)
		int maxAparelhos = T1 + T2 + T3 + T4 - 3;

		// Imprimindo o resultado
		System.out.println(maxAparelhos);

		scanner.close();
	}

}
