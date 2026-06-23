package br.com.helper.designpartterns.singleton;

/***
 * Singleton com Bloco synchronized (Lazy + thread-safe, mas lento)
 * Inicialização Lazy - Thread-Safe (Com Sincronização - “bloqueio”)
 * 
 * Para corrigir o problema do Lazy comum, adiciona-se um mecanismo de trava
 * (como o `synchronized` do Java ou `lock` do C#) no método que entrega a
 * instância. Prós: Seguro para múltiplas threads e mantém o carregamento
 * tardio. Contras: Adiciona uma perda de desempenho (*overhead*), pois toda vez
 * que qualquer thread pedir a instância, ela terá que esperar na fila da trava,
 * mesmo que a instância já tenha sido criada há muito tempo.
 * 
 */
public class SingletonLazySynchronized {
	// Atributo estático que armazenará a única instância da classe
	private static SingletonLazySynchronized instancia;

	// Construtor privado impede que outras classes usem o operador "new" fora desta
	// classe
	private SingletonLazySynchronized() {
	}

	/**
	 * Método público e estático para fornecer o ponto global de acesso à instância.
	 * O uso da palavra-chave 'synchronized' garante o Thread-Safety: Apenas uma
	 * thread pode executar este método por vez, evitando que duas threads criem
	 * duas instâncias diferentes simultaneamente.
	 */
	public static synchronized SingletonLazySynchronized getInstancia() {
		// Inicialização Lazy (Tardia): o objeto só é criado quando o método é chamado
		// pela primeira vez
		if (instancia == null) {
			instancia = new SingletonLazySynchronized();
		}
		return instancia;
	}
}
