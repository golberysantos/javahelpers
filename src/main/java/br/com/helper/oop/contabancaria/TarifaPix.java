package br.com.helper.oop.contabancaria;

public class TarifaPix implements CalculadoraTarifa {
	@Override
	public double calcular(double valor) {
		return 0.0; // Pix é gratuito
	}
}
