package br.com.helper.designpartterns.singleton;

public class SingletonHoldInternoBillPughController {
	public static  void exibir() {
		  // Obtém a instância única
        SingletonHoldInternoBillPugh s1 = SingletonHoldInternoBillPugh.getInstance();
        SingletonHoldInternoBillPugh s2 = SingletonHoldInternoBillPugh.getInstance();

        // Teste: s1 e s2 são o MESMO objeto?
        System.out.println(s1 == s2); // true ✅

        // Usa o Singleton
        s1.fazerAlgo();

        // Tentativa de quebrar via reflexão (vai lançar exceção!)
        // Constructor<SingletonHoldInternoBillPugh> c = SingletonHoldInternoBillPugh.class.getDeclaredConstructor();
        // c.setAccessible(true);
        // SingletonHoldInternoBillPugh s3 = c.newInstance(); // ❌ IllegalStateException

	}

}
