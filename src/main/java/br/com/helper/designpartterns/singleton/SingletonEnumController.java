package br.com.helper.designpartterns.singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingletonEnumController {
	public static void exibir() {
		// Acessando o Singleton
        SingletonEnum s1 = SingletonEnum.INSTANCIA;
        SingletonEnum s2 = SingletonEnum.INSTANCIA;
        
        System.out.println("s1 == s2: " + (s1 == s2)); // true
        
        s1.fazerAlgo();
        s1.fazerAlgo();
        
        System.out.println("Contador: " + s1.getContador()); // 2
        
        // Teste de thread-safety (100 threads acessando)
        ExecutorService executor = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> SingletonEnum.INSTANCIA.fazerAlgo());
        }
        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor não terminou!");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            System.err.println("Thread principal interrompida!");
        }
        
        System.out.println("Contador final (esperado 102): " + SingletonEnum.INSTANCIA.getContador());
        // Contador final: 102 ✅
	}
}
