package br.com.helper.oop.concorrencia;

public class ConcorrenciaThreadController {
	
	public void mostrarExemplo01ComExtends() {
		System.out.println("Thread Id: " + Thread.currentThread().getId());
        new ConcorrenciaThreadComExtends().start();
        new ConcorrenciaThreadComExtends().start();
        new ConcorrenciaThreadComExtends().start();

	}

}
