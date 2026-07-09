package br.com.helper.designpartterns.strategy;

//Singleton para Frete Normal
public enum FreteNormal implements FreteStrategy {
	INSTANCE; // Única instância

	@Override
	public double calcular(double peso) {
		if (peso < 0) {
			throw new IllegalArgumentException("Peso não pode ser negativo.");
		}
		return peso * 1.5;
	}
}
