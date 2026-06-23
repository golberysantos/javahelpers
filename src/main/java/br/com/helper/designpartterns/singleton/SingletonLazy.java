package br.com.helper.designpartterns.singleton;

/***
 * Inicialização Lazy (Tardia / Preguiçosa) - Não Thread-Safe A instância só é
 * criada na primeira vez que alguém pede por ela. Prós: Economiza memória se
 * nunca for utilizada. Contras: falha catastroficamente se duas threads
 * tentarem criá-la ao mesmo tempo. Em sistemas com múltiplas threads, duas
 * chamadas simultâneas podem criar duas instâncias. Não use em produção!
 * 
 */
public class SingletonLazy {
    private static SingletonLazy instancia;
    
    private SingletonLazy() {} // impede new fora da classe
    
    public static SingletonLazy getInstancia() {
        if (instancia == null) {
            instancia = new SingletonLazy();
        }
        return instancia;
    }
}
