package br.com.helper.designpartterns.singleton;

public class SingletonEager {
    private static final SingletonEager INSTANCIA = new SingletonEager();
    
    private SingletonEager() {} // impede new fora da classe
    
    public static SingletonEager getInstancia() {
        return INSTANCIA;
    }
}

