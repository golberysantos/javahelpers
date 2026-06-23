package br.com.helper.designpartterns.singleton;

/***
 * Inicialização Lazy - Thread-Safe (Com Bloqueio/Sincronização)
 * 
 * Para corrigir o problema do Lazy comum, adiciona-se um mecanismo de trava
 * (como o `synchronized` do Java ou `lock` do C#) no método que entrega a
 * instância. Prós: Seguro para múltiplas threads e mantém o carregamento
 * tardio. Contras: Adiciona uma perda de desempenho (*overhead*), pois toda vez
 * que qualquer thread pedir a instância, ela terá que esperar na fila da trava,
 * mesmo que a instância já tenha sido criada há muito tempo.
 * 
 */
public class SingletonSync {
	private static SingletonSync instancia;

	private SingletonSync() {
	}

	public static synchronized SingletonSync getInstancia() {
		if (instancia == null) {
			instancia = new SingletonSync();
		}
		return instancia;
	}
}
