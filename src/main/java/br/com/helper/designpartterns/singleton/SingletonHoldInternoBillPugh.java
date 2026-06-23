package br.com.helper.designpartterns.singleton;

import java.io.Serializable;


public final class SingletonHoldInternoBillPugh implements Serializable {
    
    private static final long serialVersionUID = 1L; // ✅ Adicionado!
    
    private SingletonHoldInternoBillPugh() {
        if (Holder.INSTANCIA != null) {
            throw new IllegalStateException("Singleton já existe!");
        }
    }
    
    private static class Holder {
        private static final SingletonHoldInternoBillPugh INSTANCIA = new SingletonHoldInternoBillPugh();
    }
    
    public static SingletonHoldInternoBillPugh getInstance() {
        return Holder.INSTANCIA;
    }
    
    protected Object readResolve() {
        return getInstance();
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton não pode ser clonado");
    }
    
    public void fazerAlgo() {
        System.out.println("SingletonHoldInternoBillPugh executando ação...");
    }
}
