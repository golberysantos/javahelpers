package br.com.helper.oop.concorrencia;

public class ConcorrenciaThreadComRunnable implements Runnable {
	@Override
	public void run() {
		var threadId = Thread.currentThread().getId();

		for (int i = 0; i < 10; i++) {
			System.out.println("Thread Id: " + threadId + ", Value: " + i);
		}
	}
}
