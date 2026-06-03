package br.com.helper.oop.concorrencia;

public class Exemplo01MyThreadComExtends extends Thread {
	@Override
    public void run() {
		
        var threadId = Thread.currentThread().getId();
        for (int i = 0; i < 3; i++) {
            System.out.println("Thread Id: " + threadId + ", Value: " + i);
        }
    }
}
