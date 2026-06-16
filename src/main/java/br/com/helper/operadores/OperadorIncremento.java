package br.com.helper.operadores;

public class OperadorIncremento {

	public void incremento() {
		int b;

		System.out.println("Pós Incremento");
		b = 5;
		System.out.println(b++); // imprime 5
		System.out.println(b); // imprime 6

		System.out.println("Pré incremento");
		b = 5;
		System.out.println(++b); // imprime 6
		System.out.println(b); // imprime 6
	}

}
