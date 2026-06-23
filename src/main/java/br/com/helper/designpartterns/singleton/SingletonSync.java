package br.com.helper.designpartterns.singleton;

public class SingletonSync {
    private static SingletonSync instancia;
    
    private SingletonSync() {}
    
    public static synchronized SingletonSync getInstancia() {
        if (instancia == null) {
            instancia = new SingletonSync();
        }
        return instancia;
    }
}

