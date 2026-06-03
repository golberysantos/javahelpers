package br.com.helper.oop.concorrencia;

public class ConcorrenciaThread {
	
	private void mostrarExemplo01ComExtends() {
		System.out.println("Main Thread Id: " + Thread.currentThread().getId());
        new Exemplo01MyThreadComExtends().start();
        new Exemplo01MyThreadComExtends().start();
        new Exemplo01MyThreadComExtends().start();

	}

}
