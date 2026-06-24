package br.com.helper.designpartterns.strategy;

import java.util.Objects;

public final class CalculadoraFrete { // 'final' impede herança
	private final FreteStrategy strategy;

	// Construtor obrigatório (sem setter). A estratégia é definida uma vez.
	public CalculadoraFrete(FreteStrategy strategy) {
		this.strategy = Objects.requireNonNull(strategy, "Estratégia não pode ser nula.");
	}

	public double calcularFrete(double peso) {
		if (peso < 0) {
			throw new IllegalArgumentException("Peso não pode ser negativo.");
		}
		return strategy.calcular(peso);
	}
}
