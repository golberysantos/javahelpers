package br.com.helper.designpartterns.strategy;

public class MainProfissional {
	// Fábrica que devolve a estratégia correta
	public static FreteStrategy obterEstrategia(String tipo) {
		if ("NORMAL".equalsIgnoreCase(tipo)) {
			return FreteNormal.INSTANCE;
		} else if ("EXPRESSO".equalsIgnoreCase(tipo)) {
			return FreteExpresso.INSTANCE;
		}
		// Pode-se retornar uma estratégia padrão ou lançar exceção
		throw new IllegalArgumentException("Tipo de frete inválido.");
	}

	public void executarEstrategia() {
		double peso = 10.0;

		// O cliente obtém a estratégia da fábrica e injeta no contexto
		FreteStrategy estrategiaNormal = obterEstrategia("NORMAL");
		CalculadoraFrete calcNormal = new CalculadoraFrete(estrategiaNormal);
		System.out.println("Frete Normal: " + calcNormal.calcularFrete(peso));

		FreteStrategy estrategiaExpresso = obterEstrategia("EXPRESSO");
		CalculadoraFrete calcExpresso = new CalculadoraFrete(estrategiaExpresso);
		System.out.println("Frete Expresso: " + calcExpresso.calcularFrete(peso));

	}

}
