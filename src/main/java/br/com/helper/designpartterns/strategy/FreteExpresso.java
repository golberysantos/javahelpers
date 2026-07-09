package br.com.helper.designpartterns.strategy;

//Singleton para Frete Expresso
public enum FreteExpresso implements FreteStrategy {
	INSTANCE;

	@Override
	public double calcular(double peso) {
		if (peso < 0) {
			throw new IllegalArgumentException("Peso não pode ser negativo.");
		}
		return peso * 3.0;
	}
}
