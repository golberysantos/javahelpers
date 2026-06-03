package br.com.helper.oop.concorrencia;

public class ConcorrenciaThread {
	
	private void exemplo01() {
		System.out.println("Main Thread Id: " + Thread.currentThread().getId());
        new Exemplo01MyThread().start();
        new Exemplo01MyThread().start();
        new Exemplo01MyThread().start();

	}

}
