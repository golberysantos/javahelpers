package br.com.helper.designpartterns.singleton;

import java.io.Serializable;

public final class SingletonHoldInternoBillPugh implements Serializable {
	private SingletonHoldInternoBillPugh() {
		// Proteção contra reflexão
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

	// Proteção contra serialização
	protected Object readResolve() {
		return getInstance();
	}

	// Proteção contra clone
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton não pode ser clonado");
	}

	// Método de exemplo para demonstrar uso
	public void fazerAlgo() {
		System.out.println("SingletonHoldInternoBillPugh executando ação...");
	}
}
